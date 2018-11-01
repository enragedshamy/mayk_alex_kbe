package de.htw.ai.kbe.runmerunner;

import java.util.Date;

public abstract class TestClassWithRunMes {
	
	@RunMe
	public abstract void abstractMethod();
	
	@RunMe
	private void doNothing() {
		
	}
	
	@RunMe
	public static void staticMethod(Date date) {
		System.out.println(date.getTime());
	}
	
	@RunMe @AnotherAnnotation
	public boolean isOneEqualToOne(int integer) {
		return true;
	}
	
	public String welcome() {
		return "welcome";
	}

}
