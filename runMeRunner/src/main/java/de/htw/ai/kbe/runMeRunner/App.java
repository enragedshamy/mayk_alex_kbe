package de.htw.ai.kbe.runMeRunner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	RunMeFinder rmf = new RunMeFinder(TestClassWithRunMes.class);
    	try {
			rmf.printAnnotatedMethods();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
