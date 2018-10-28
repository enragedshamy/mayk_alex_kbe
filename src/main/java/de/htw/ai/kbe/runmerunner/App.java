package de.htw.ai.kbe.runmerunner;

import org.apache.commons.cli.ParseException;


public class App {

    public static void main(String[] args) {
    	
    	CLParser parser = new CLParser();
    	try {
			if (parser.parsingSuccessful(args)) {
			    // RunMeFinder rmf = new RunMeFinder(TestClassWithRunMes.class);
				// -c de.htw.ai.kbe.runmerunner.TestClassWithRunMes
				RunMeFinder rmf = new RunMeFinder(parser.getGivenClass(), parser.getFileName());
			    rmf.findAnnotatedMethodsAndWriteToFile();
			}
		} catch (ParseException | ClassNotFoundException e) {
			e.printStackTrace();
		}

    }
}
