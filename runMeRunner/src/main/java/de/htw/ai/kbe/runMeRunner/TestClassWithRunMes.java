package de.htw.ai.kbe.runMeRunner;


public class TestClassWithRunMes {
	
	@RunMe
	private void doNothing() {
		
	}
	
	@RunMe @AnotherAnnotation
	public boolean isOneEqualToOne() {
		return true;
	}
	
	public String welcome() {
		return "welcome";
	}

}
