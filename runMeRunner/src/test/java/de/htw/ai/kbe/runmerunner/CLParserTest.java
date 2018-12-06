package de.htw.ai.kbe.runmerunner;

import static org.junit.Assert.*;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class CLParserTest {

	@Test
	public void parsingSuccessfulWithCorrectInputTest() {
		Boolean bool = false;
		try {
			bool = (new CLParser().parsingSuccessful(new String[] {"-c","de.htw.ai.kbe.runmerunner.TestClassWithRunMes"}));
		} catch (ParseException e) {
		}
		assertTrue(bool);
		bool = false;
		try {
			bool = (new CLParser().parsingSuccessful(new String[] {"-c","de.htw.ai.kbe.runmerunner.TestClassWithRunMes", "-o","report.txt"}));
		} catch (ParseException e) {
		}
		assertTrue(bool);
	}
	
	@Test
	public void parsingSuccessfulWithIncorrectInputTest() {
		Boolean bool = true;
		try {
			bool = (new CLParser().parsingSuccessful(new String[] {"-c","de.htw.ai.kbe.runmerunner.TestClassWithRunMes","-o"}));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertFalse(bool);
		bool = true;
		try {
			bool = (new CLParser().parsingSuccessful(new String[] {"-c","de.htw.ai.kbe.runmerunner.TestClassWithRunMes","-o",""}));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertFalse(bool);
		bool = true;
		try {
			bool = (new CLParser().parsingSuccessful(new String[] {"-c",""}));
		} catch (ParseException e) {
		}
		assertFalse(bool);
		bool = true;
		try {
			bool = (new CLParser().parsingSuccessful(new String[] {"blablablub"}));
		} catch (ParseException e) {
		}
		assertFalse(bool);
	}
	
	

}
