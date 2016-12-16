#Spreadsheet Handler

A library for easing the reading and writing of Excel and CSV files.

##Use

###Setup Model

Spreadsheet Handler uses JPA/JaxP inspired annotations to process spreadsheets.
The models are mapped one sheet at a time. To get started, use the @Column annotation
on the fields you want to map to the sheet. The column index is required, the Header string
is completely optional and can only be used during writing.

Example domain:

```java
public class Person {

	@Column(index = 1, header = "First name")
	private String firstName;

	@Column(index = 1, header = "Last name")
	private String lastName;

	@Column(index = 4, header = "Age")
	private int age;

	//optional getters and setters ect...
}
```

###Create Handler

To use the model, you must pass the model class to SpreadsheetHandler.createSpreadsheetHandler.
The Handler that is returned is threadsafe, and it is recommended that it be cached and reused.

```java
SpreadsheetHandler<Person> handler = SpreadsheetHandler.createSpreadsheetHandler(Person.class);
```

###Reading Spreadsheets

####Create Reader
With the SpreadsheetHandler you can call a series of methods to read spreadsheets.
Each of these methods returns a SpreadsheetReader of the same type as the Handler.
Readers are NOT threadsafe and implement AutoCloseable. As such they should be closed when no longer needed.
Readers will close their given resources properly. The ones that take InputStreams will automatically
wrap them in BufferedInputStreams if needed.

"createReader" - Takes a File and uses the extension to try and read as CSV or Excel. Fails if the file was neither or some IO exception happened.

"createCSVReader" - Can take a File, and InputStream or an OpenCSV CSVReader (useful if you need special parsing properties).

"createExcelReader" - Mostly the same as CSV, except that each of it's methods can also accept the sheet index to use. It can read both Excel 2003 ".xsl" and Excel 2010 ".xslx" files equally well. The default is the first sheet. Apache POI WorkBook is the specific model if you need to perform special actions or parsing.

####Using the Reader.
SpreadsheetReader has a series of methods to read data, most of which can throw IOExceptions.
When reading, empty cells are returned as null.


readLine - Reads a single line and parses it, returning a model object.

readRawLine - Reads a single line and does NOT parse it, returning a list of Strings.

skipLine - Skips over one line, not reading or parsing it.

skipLines - Skips a number of lines.

readRemainingLines - Reads all lines and returns them as a Stream of model objects. These will typically be read lazily.

readRemainingLinesAsList - Reads all lines and returns them as a List. This means it will read the whole file eagerly, unlike the above.

readChunk - Reads a number of lines supplied and returns them as a list. If there are not enough lines in the file the list will be short.


###Writing Spreadsheets

####Create Writer
Creating a SpreadsheetWriter is mostly a mirror of creating a Reader, calling methods on a SpreadsheetHandler, with a few differences.

Excel writers can't take an index. The only option is to write to the first page.

There are separate methods for writing to Excel 2003 and Excel 2010 formats. The 2003 format is called "Legacy" in the method names.

Just like Readers, writers are NOT threadsafe and should be closed. Also like Readers they will close their own resources and OutputStreams will be buffered automatically.

####Using the Writer

writeRow - Takes a model object and writes it to the sheet as one row.

writeAllRows - Overloads for Iterable, Iterator and Stream of model object, writes each one out to the file in order.

writeHeaders - Writes our the header values from the @Column annotations on the model. Leaves gaps where there is no header value.

writeEmpty - Writes a totally empty line.

writeFreeFormRow - Overloads for List\<String\> or String[] that writes the strings out literally to a single row.

writeFreeFormRows - Takes a List\<List\<String\>\> and writes each inner list as a free form row.



###Type Converters

Model objects can map fields with any primitive type or String by default.
If you want to map other types, you can do so by supplying a TypeConverter for the type
when creating the SpreadsheetHandler.


Creating your own TypeConverter is very simple. First you make a new class and implement the TypeConverter interface.
The interface is Generic upon the type you wish to convert.

There are three methods in TypeConverter.

```java
public interface TypeConverter<T> {

	Class<T> type();

	T parse(String raw);

	String serialize(T obj);

}
```

type - returns the Class of the type you want to convert and is used to find the right converter for a field.

parse - takes a String of data that must be parsed.
The raw String is guaranteed never to be null, empty or blank (containing only whitespace).
If the TypeConverter can't parse the raw String, it can throw any kind of exception, which will be handled inside the framework.

serialize - takes an object and expects the String from of it. The given obj is guaranteed never to be null.


Just pass a list of these converters to the createSpreadsheetHandler method.

```java
SpreadsheetHandler<Person> handler = SpreadsheetHandler.createSpreadsheetHandler(Person.class, new MyCustomTypeConverter());
```

Passing a Converter for a Wrapper type will override the default primitive type.


```java

public class YNBooleanConverter implements TypeConverter<Boolean> {

	// parse Y == true and N == false ....

}

SpreadsheetHandler<Person> handler = SpreadsheetHandler.createSpreadsheetHandler(Person.class, new YNBooleanConverter());
```


###Supplied TypeConverter Bases

There are a few TypeConverter object that you can use that aren't supplied by default.

####DateConverter

DateConverter takes a DateTimeFormatter or a String that contains a DateTimeFormatter pattern.
It will use that pattern to parse and serialize java.util.Date objects. You can only have one Date format per handler.

```java
SpreadsheetHandler<Person> handler = SpreadsheetHandler.createSpreadsheetHandler(Person.class, new DateConverter("dd-MM-uuuu"));
```

####EnumConverter

EnumConverter takes an Enum class and can convert based off the Enum's names.
The parsing is case insensitive. Also has a static 'of' factory. Invalid
values will become null.

```java
SpreadsheetHandler<Person> handler = SpreadsheetHandler.createSpreadsheetHandler(Person.class, new EnumConverter(SomeEnum.class), EnumConverter.of(OtherEnum.class));
```

###Validation

Beyond just parsing there might be other kinds of validation
you may want to put through your input, such as ensuring values are
not null or numbers are above or below certain limits or more complex
forms of validation. This can be made easy with
JSR 301 and the BeanValidation api. All you have to do is pass in a
ValidatorFactory when creating the SpreadsheetHandler.

You will have to include your own BeanValidation implementation,
such as Hibernate-Validation, as we don't provide one here.

The Spreadsheet Reader will pass each row to the Validator
after parsing and before returning it, and it will start throwing
javax.validtaion.ConstraintViolationException for invalid rows.

See the BeanValidation documentation for more information on annotations
and usage.


###Error Handling


####IOExceptions

Various kinds of IOExceptions can occur at nearly any time during reading or writing and are considered fatal.
As such, there is no fall back mechanism and it is recommended that the entire file processing scope be inside
a single try catch.

```java
try (SpreadsheetReader<Person> reader = handler.createReader(file)) {

	//Do all reading here

}
```

####By row parsing exceptions.

During the parsing of each row while reading, invalid data can cause exceptions,
such as a ConversionException during TypeConversion, or a ValidationException for
invalid data.

#####Default behaviour

By default, any of these cases will cause a RowParseException to be throw.

If you're reading one row at a time with readLine or reading lines as a list, it will come from that method.

RowParseException has a cause that should be one of the above types, and they should contain more data about the cause.

If you're using the Stream though, the exception will come from the terminal
operation and it will likely be harder to track and recover from.
Unless you want to outright reject Spreadsheets with invalid data it
is recommended you use the ErrorHandler api.

####Using an ErrorHandler

ErrorHandler is a FunctionalInterface you can pass to the SpreadsheetHandler when you create a SpreadsheetReader.


```java
SpreadsheetReader<Person> reader = handler.createReader(file, errorData -> {/* Handle error */});
```

ErrorHandler is a very basic consumer that takes an ErrorData and returns void. Error data looks like this.

```java
public interface ErrorData {

	//The raw Strings of the row that caused the error.
	List<String> getRawRow();

	//A log-ready message for the cause
	String getMessage();

	//The full exception for what happened.
	Throwable getCause();

}
```

From here you can deal with the exception any way you wish, and it will be used for
all cases, single line, list based and Stream based.

An important note is that the Throwable returned by getCause() is NOT a
RowParseException, but the inner ConversionException or ConstraintViolationException.

Also note there is a third cause, a ReflectionException that wraps when
calling the getters or setters via the reflection api fails. This one can come from the
writer as well as reader, but is honestly very rare and really is a sign that something
is very wrong with your model class. Still it might be nice to know that in case you do see one.

###Tips and Tricks

The SpreadsheetReader will skip any row that only contains whitespace,
to avoid errors at the bottom of the sheet where there might be invisible
data. But be warned that a single value that's not inside the normal dataset will
not trigger this.

Example:

|Age | Name | |
|:---|:---|:---
|34 | Dave | |
| | | |
|26 | Susan |
| |  |junk |
|29 | Mary | |

The gap between Dave and Susan will be skipped, as it's empty,
but the row between Susan and Mary will NOT be skipped as there is a value in that
row, even though it doesn't belong in the data set.