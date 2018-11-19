package de.htw.ai.kbe.runmerunner;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RunMeFinderTest {

    private RunMeFinder underTest;

    @Test
    public void testClass() {
        underTest = new RunMeFinder(TestClass.class, "");
        underTest.execute();

        assertEquals(3, underTest.getMethodsWithRunMe().size());
        assertEquals(2, underTest.getMethodsWithOutRunMe().size());

        HashMap<String, String> methodsGeneratingExceptions = underTest.getMethodsGeneratingExceptions();
        assertEquals(1, methodsGeneratingExceptions.size());
        assertTrue(methodsGeneratingExceptions.containsKey("foo2"));
    }

    @Test
    public void abstractTestClass() {
        underTest = new RunMeFinder(AbstractTestClass.class, "");
        underTest.execute();

        assertEquals(3, underTest.getMethodsWithRunMe().size());
        assertEquals(1, underTest.getMethodsWithOutRunMe().size());

        HashMap<String, String> methodsGeneratingExceptions = underTest.getMethodsGeneratingExceptions();
        assertEquals(3, methodsGeneratingExceptions.size());
        assertTrue(methodsGeneratingExceptions.containsKey("foo2"));
    }
}