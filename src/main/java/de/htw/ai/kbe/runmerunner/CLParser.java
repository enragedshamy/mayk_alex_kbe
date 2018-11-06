package de.htw.ai.kbe.runmerunner;

import org.apache.commons.cli.*;

public class CLParser {
	private CommandLine cmd;
	private String file = "report.txt";
	
	public boolean parsingSuccessful(String[] args) throws ParseException {
		boolean wrongOption = false;
		String unrecognized = "";

		// defining stage
		Options options = new Options();
		Option clazz = Option.builder().longOpt("c").argName("Class").hasArg().desc("use given input class").build();
		Option report = Option.builder().longOpt("o").argName("Output").hasArg().desc("write in given file").build();
		options.addOption(clazz).addOption(report);

		// parsing stage
		cmd = null;
		if (args.length %2 != 0) {
			System.out.println("Parsing not possible. Each command line parameter needs an argument.");
			return false;
		}
		else {
			try {
				CommandLineParser parser = new DefaultParser();
				cmd = parser.parse(options, args);
			}
			catch (UnrecognizedOptionException e) {
				wrongOption = true;
				unrecognized = e.getOption();
			}
		}


		// interrogation stage    kein Hauptmanifestattribut, in runMeRunner-0.0.1-SNAPSHOT.jar

		HelpFormatter formatter = new HelpFormatter();
		if (wrongOption) {
			System.out.println(unrecognized + " isn't a valid option.");
			formatter.printHelp("runMeRunner", options);
			return false;
		}
		else {
			if (!cmd.hasOption("c")) {
				formatter.printHelp("runMeRunner", options);
				System.out.println("you must use option c");
				return false;
			} 
			else  {
				if (cmd.getOptionValue( "c" ).isEmpty() ) {
					System.out.println("Option c may not be empty.");
					return false;
				}
				 System.out.println( cmd.getOptionValue( "c" ) );
					if (!cmd.hasOption("o")) {
						System.out.println("Option o not indicated. Default file report.txt is used. ");
					}
					else {
						if (cmd.getOptionValue( "o" ).isEmpty()) {
							System.out.println("Option o indicated, but empty.");
							return false;
						}
						file = cmd.getOptionValue( "o" );
					}
			}
		}
		return true;
	}

	public String getFileName() {
		return file;
	}
	
	public Class getGivenClass() throws ClassNotFoundException {
		return Class.forName(cmd.getOptionValue( "c" ));
	}
	
}