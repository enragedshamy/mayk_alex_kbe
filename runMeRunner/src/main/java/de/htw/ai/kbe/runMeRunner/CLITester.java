package de.htw.ai.kbe.runMeRunner;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;



public class CLITester {

	public static void main(String[] args) throws ParseException {
		boolean wrongOption = false;
		String unrecognized = "";

		// defining stage
		Options options = new Options();
		Option clazz = Option.builder().longOpt("c").argName("String").hasArg().desc("use given input class").build();
		Option report = Option.builder().longOpt("o").argName("String").hasArg().desc("write in given file").build();
		options.addOption(clazz).addOption(report);

		// parsing stage
		CommandLine cmd = null;
		try {
			CommandLineParser parser = new DefaultParser();
			cmd = parser.parse(options, args);
		}
		catch (UnrecognizedOptionException e) {
			wrongOption = true;
			unrecognized = e.getOption();
		}

		// interrogation stage
		HelpFormatter formatter = new HelpFormatter();
		if (wrongOption) {
			System.out.println(unrecognized + " isn't a valid option.");
			formatter.printHelp("CLITester", options);
		}
		else {
			if (!cmd.hasOption("c")) {
				formatter.printHelp("CLITester", options);
				System.out.println("you must use option c");
			} 
			else  {
				//Properties properties = cmd.getOptionProperties("clazz");
				//System.out.println(properties.getProperty("c"));
				 System.out.println( cmd.getOptionValue( "c" ) );
					if (!cmd.hasOption("o")) {
						System.out.println("Option o not indicated. Default file report.txt is used. ");
					}
					else {
						System.out.println( cmd.getOptionValue( "o" ) );
					}
			}
		}
	
	}
}