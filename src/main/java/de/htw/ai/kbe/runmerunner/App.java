package de.htw.ai.kbe.runmerunner;

public class App {

    public static void main(String[] args) {

        RunMeFinder rmf = new RunMeFinder(TestClassWithRunMes.class);

        rmf.findAnnotatedMethodsAndWriteToFile();
    }
}
