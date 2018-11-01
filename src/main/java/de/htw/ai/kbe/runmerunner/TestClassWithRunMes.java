package de.htw.ai.kbe.runmerunner;

public class TestClassWithRunMes {

	@RunMe
	private void doNothing() {

	}

	@RunMe @AnotherAnnotation
	public boolean isOneEqualToOne(int integer) {
		return true;
	}
	
	public String welcome() {
		return "welcome";
	}

}
