package de.htw.ai.kbe.runmerunner;

public class App {

    public static void main(String[] args) {

        CLParser parser = new CLParser();
        try {
            if (parser.parsingSuccessful(args)) {
            	try {
            		System.out.println("Looking for: " + parser.getGivenClass());
            		new RunMeFinder(parser.getGivenClass(), parser.getFileName()).execute();
            	}
            	catch (ClassNotFoundException e) {
            		System.out.println("Class not found.");
            	}
            } else {
                //new RunMeFinder(TestClassWithRunMes.class, null).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
