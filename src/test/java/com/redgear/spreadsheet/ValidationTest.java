package com.redgear.spreadsheet;

import com.redgear.spreadsheet.exceptions.RowParseException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
@SuppressWarnings("Duplicates")
public class ValidationTest {

	private static final Logger log = LoggerFactory.getLogger(ValidationTest.class);

	private <T> T logObj(String message, T obj) {
		log.info(message, obj);
		return obj;
	}

	@Test
	public void basicWriterReaderTest() throws Exception {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

		TestBeanBasic person1 = new TestBeanBasic("Dave", "Dave@someplace.net", "Mr.", 26);
		TestBeanBasic person2 = new TestBeanBasic("Mary", "Mary@someplace.net", "Mrs.", 47);
		TestBeanBasic person3 = new TestBeanBasic("Frank", "Frank@someplace.net", "Sir.", 43);
		TestBeanBasic person4 = new TestBeanBasic("Susan", "Suzzy@someplace.net", "Miss.", 21);
		TestBeanBasic person5 = new TestBeanBasic("Hank", "Hankanator@someplace.net", "Dr.", 18);


		File temp = new File("build/test/validationTest.csv");

		Files.deleteIfExists(temp.toPath());
		Files.createDirectories(temp.toPath().getParent());

		//Make sure to pass in the validatorFactory to enable validation.
		SpreadsheetHandler<TestBeanBasic> testBeanHandler = SpreadsheetHandler.createSpreadsheetHandler(TestBeanBasic.class, validatorFactory);


		try (SpreadsheetWriter<TestBeanBasic> testBeanWriter = testBeanHandler.createCsvWriter(temp)) {

			testBeanWriter.writeFreeFormRow(Collections.singletonList("People"));
			testBeanWriter.writeHeaders();
			testBeanWriter.writeRow(person1);
			testBeanWriter.writeAllRows(Arrays.asList(person2, person3, person4));
			testBeanWriter.writeRow(person5);
		}

		//Write out just to clarify.

		try (SpreadsheetReader<TestBeanBasic> testBeanReader = testBeanHandler.createCsvReader(temp);){

			testBeanReader.skipLine(); //Main title
			testBeanReader.skipLine(); //Headers
			assertEquals(person1, logObj("Person1: {}", testBeanReader.readLine()));
			assertEquals(person2, logObj("Person2: {}", testBeanReader.readLine()));

			//Using stream to test that functionality, but turning it to an iterator to better test.
			Iterator<TestBeanBasic> it = testBeanReader.readRemainingLines().iterator();

			assertEquals(person3, logObj("Person3: {}", it.next()));
			assertEquals(person4, logObj("Person4: {}", it.next()));

			try {
				assertEquals(person5, logObj("Person5: {}", it.next()));
			} catch (RowParseException e) {
				log.info("Successfully validated input. Got exception: ", e);
				return;
			}

			fail("We should have failed due to invalid age of Hank");
		}
	}

}
