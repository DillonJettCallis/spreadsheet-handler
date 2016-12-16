package com.redgear.spreadsheet;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
@SuppressWarnings("Duplicates")
public class ExcelTest {

	private static final Logger log = LoggerFactory.getLogger(ExcelTest.class);

	private <T> T logObj(String message, T obj) {
		log.info(message, obj);
		return obj;
	}

	@Test
	public void basicWriterReaderTest() throws Exception {

		TestBeanBasic person1 = new TestBeanBasic("Dave", "Dave@someplace.net", "Mr.", 26);
		TestBeanBasic person2 = new TestBeanBasic("Mary", "Mary@someplace.net", "Mrs.", 47);
		TestBeanBasic person3 = new TestBeanBasic("Frank", "Frank@someplace.net", "Sir.", 43);
		TestBeanBasic person4 = new TestBeanBasic("Susan", "Suzzy@someplace.net", "Miss.", 21);
		TestBeanBasic person5 = new TestBeanBasic("Hank", "Hankanator@someplace.net", "Dr.", 82);


		File temp = new File("build/test/testFile.xlsx");

		Files.deleteIfExists(temp.toPath());
		Files.createDirectories(temp.toPath().getParent());

		SpreadsheetHandler<TestBeanBasic> testBeanHandler = SpreadsheetHandler.createSpreadsheetHandler(TestBeanBasic.class);


		try (SpreadsheetWriter<TestBeanBasic> testBeanWriter = testBeanHandler.createExcelWriter(temp);) {
			testBeanWriter.writeFreeFormRow(Collections.singletonList("People"));
			testBeanWriter.writeHeaders();
			testBeanWriter.writeRow(person1);
			testBeanWriter.writeAllRows(Arrays.asList(person2, person3, person4));
			testBeanWriter.writeRow(person5);
		}


		try (SpreadsheetReader<TestBeanBasic> testBeanReader = testBeanHandler.createExcelReader(temp)) {
			testBeanReader.skipLine(); //Main title
			testBeanReader.skipLine(); //Headers
			assertEquals(person1, logObj("Person1: {}", testBeanReader.readLine()));
			assertEquals(person2, logObj("Person2: {}", testBeanReader.readLine()));

			//Using stream to test that functionality, but turning it to an iterator to better test.
			Iterator<TestBeanBasic> it = testBeanReader.readRemainingLines().iterator();

			assertEquals(person3, logObj("Person3: {}", it.next()));
			assertEquals(person4, logObj("Person4: {}", it.next()));
			assertEquals(person5, logObj("Person5: {}", it.next()));
		}
	}

	@Test
	public void basicLegacyWriterReaderTest() throws Exception {

		TestBeanBasic person1 = new TestBeanBasic("Dave", "Dave@someplace.net", "Mr.", 26);
		TestBeanBasic person2 = new TestBeanBasic("Mary", "Mary@someplace.net", "Mrs.", 47);
		TestBeanBasic person3 = new TestBeanBasic("Frank", "Frank@someplace.net", "Sir.", 43);
		TestBeanBasic person4 = new TestBeanBasic("Susan", "Suzzy@someplace.net", "Miss.", 21);
		TestBeanBasic person5 = new TestBeanBasic("Hank", "Hankanator@someplace.net", "Dr.", 82);


		File temp = new File("build/test/testFile.xls");

		Files.deleteIfExists(temp.toPath());
		Files.createDirectories(temp.toPath().getParent());

		SpreadsheetHandler<TestBeanBasic> testBeanHandler = SpreadsheetHandler.createSpreadsheetHandler(TestBeanBasic.class);


		try (SpreadsheetWriter<TestBeanBasic> testBeanWriter = testBeanHandler.createLegacyExcelWriter(temp);) {
			testBeanWriter.writeFreeFormRow(Collections.singletonList("People"));
			testBeanWriter.writeHeaders();
			testBeanWriter.writeRow(person1);
			testBeanWriter.writeAllRows(Arrays.asList(person2, person3, person4));
			testBeanWriter.writeRow(person5);
		}


		try (SpreadsheetReader<TestBeanBasic> testBeanReader = testBeanHandler.createExcelReader(temp)) {
			testBeanReader.skipLine(); //Main title
			testBeanReader.skipLine(); //Headers
			assertEquals(person1, logObj("Person1: {}", testBeanReader.readLine()));
			assertEquals(person2, logObj("Person2: {}", testBeanReader.readLine()));

			//Using stream to test that functionality, but turning it to an iterator to better test.
			Iterator<TestBeanBasic> it = testBeanReader.readRemainingLines().iterator();

			assertEquals(person3, logObj("Person3: {}", it.next()));
			assertEquals(person4, logObj("Person4: {}", it.next()));
			assertEquals(person5, logObj("Person5: {}", it.next()));
		}
	}


	@Test
	public void headerBasedReaderTest() throws Exception {
		log.info("Starting headerBasedReaderTest");

		TestBeanBasic person1 = new TestBeanBasic("Dave", "Dave@someplace.net", "Mr.", 26);
		TestBeanBasic person2 = new TestBeanBasic("Mary", "Mary@someplace.net", "Mrs.", 47);
		TestBeanBasic person3 = new TestBeanBasic("Frank", "Frank@someplace.net", "Sir.", 43);
		TestBeanBasic person4 = new TestBeanBasic("Susan", "Suzzy@someplace.net", "Miss.", 21);
		TestBeanBasic person5 = new TestBeanBasic("Hank", "Hankanator@someplace.net", "Dr.", 82);


		File temp = new File("build/test/testFile2.xlsx");

		Files.deleteIfExists(temp.toPath());
		Files.createDirectories(temp.toPath().getParent());

		SpreadsheetHandler<TestBeanBasic> testBeanHandler = SpreadsheetHandler.createSpreadsheetHandler(TestBeanBasic.class);


		SpreadsheetWriter<TestBeanBasic> testBeanWriter = testBeanHandler.createExcelWriter(temp);



		testBeanWriter.writeFreeFormRow(Collections.singletonList("People"));
		testBeanWriter.writeFreeFormRow("age", "name", "   title  ", "EMaIl");

		for(TestBeanBasic person : Arrays.asList(person1, person2, person3, person4, person5)) {
			testBeanWriter.writeFreeFormRow(String.valueOf(person.age), person.name, person.getTitle(), person.getEmail());
		}
		testBeanWriter.close();


		SpreadsheetReader<TestBeanBasic> testBeanReader = testBeanHandler.createExcelReader(temp);

		testBeanReader.skipLine(); //Main title
		testBeanReader.parseHeaders(); //Headers
		assertEquals(person1, logObj("Person1: {}", testBeanReader.readLine()));
		assertEquals(person2, logObj("Person2: {}", testBeanReader.readLine()));

		//Using stream to test that functionality, but turning it to an iterator to better test.
		Iterator<TestBeanBasic> it = testBeanReader.readRemainingLines().iterator();

		assertEquals(person3, logObj("Person3: {}", it.next()));
		assertEquals(person4, logObj("Person4: {}", it.next()));
		assertEquals(person5, logObj("Person5: {}", it.next()));

		testBeanReader.close();

		log.info("Ending headerBasedReaderTest");
	}
}
