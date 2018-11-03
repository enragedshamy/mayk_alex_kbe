package de.htw.ai.kbe.runmerunner;

public class App {

    public static void main(String[] args) {

        CLParser parser = new CLParser();
        try {
            if (parser.parsingSuccessful(args)) {
                // RunMeFinder rmf = new RunMeFinder(TestClassWithRunMes.class);
                // -c de.htw.ai.kbe.runmerunner.TestClassWithRunMes
                new RunMeFinder(parser.getGivenClass(), parser.getFileName()).execute();
            } else {
                new RunMeFinder(TestClassWithRunMes.class, null).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
