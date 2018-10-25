package de.htw.ai.kbe.runMeRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class RunMeFinder {
	
	private final Class clazz;
	private final String annotationClass = "de.htw.ai.kbe.runMeRunner.RunMe";
	
	public RunMeFinder(Class clazz) {
		this.clazz = clazz;
	}
	
	public void printAnnotatedMethods() throws ClassNotFoundException {
		System.out.println("The following methods are annotated with @RunMe: ");
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			m.setAccessible(true);
			System.out.println(m.getName() );

			Annotation[] annotations = m.getAnnotations();
			System.out.println(annotations[0]);
//			if (annotations.length > 0) {
//				for (Annotation annotation : annotations) {
//					System.out.println(annotation.getClass().getSimpleName());
//					if ( (RunMe.class.getName()).equals(Class.forName(annotationClass).getName()) )
//						System.out.println(m.getName());
//				}
//			}
		}
		System.out.println();
	}
	

}
