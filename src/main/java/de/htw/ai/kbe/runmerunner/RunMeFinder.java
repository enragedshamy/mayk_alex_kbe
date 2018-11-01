package de.htw.ai.kbe.runmerunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RunMeFinder {

    private final Class clazz;
    private final String fileName;
    private List<Method> methodsWithRunMe;

    public RunMeFinder(Class clazz, String fileName) {
        this.clazz = clazz;
        this.fileName = fileName;
    }

    public void findAnnotatedMethodsAndWriteToFile() {
        methodsWithRunMe = new ArrayList<>();
        List<Method> methodsWithOutRunMe = new ArrayList<>();

        System.out.println("\nSearching for methods of Class: " + clazz.getName() + "\n");
        Stream.of(clazz.getDeclaredMethods())
                .forEach(method -> {
                            method.setAccessible(true);
                            boolean flag = true;
                            for (Annotation annotation : method.getAnnotations()) {
                                if (annotation.annotationType().getName().equals(RunMe.class.getName())) {
                                    methodsWithRunMe.add(method);
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                methodsWithOutRunMe.add(method);
                            }
                        }
                );

        writeOutputToFile(methodsWithRunMe, methodsWithOutRunMe);
    }

    public void writeOutputToFile(List<Method> methodsWithRunMe, List<Method> methodsWithOutRunMe) {
        StringBuilder toWrite = new StringBuilder();

        toWrite.append("Methods without @RunMe:\n");
        methodsWithOutRunMe.forEach(method ->
                toWrite
                        .append("\t")
                        .append(method.getName())
                        .append("\n")
        );

        toWrite.append("\n");

        toWrite.append("Methods with @RunMe:\n");
        methodsWithRunMe.forEach(method ->
                toWrite
                        .append("\t")
                        .append(method.getName())
                        .append("\n")
        );
        writeToFile(toWrite.toString());
    }
    
    public void runAnnotatedMethodsAndWriteExceptionsToFile() {
    	StringBuilder toWrite = new StringBuilder();
    	toWrite.append("Methods which couldn't be invocated:");
    	Object object = null;
		try {
			 if (isInvocable()) {
				 object = clazz.newInstance();
				 for (Method method : methodsWithRunMe) {
		    		 try {

			    		 if (!Modifier.isAbstract(method.getModifiers())) {
				    		 System.out.println("lasse Methode " + method.getName() + " laufen");
				    		 method.invoke(object, createParameters(method.getParameterTypes()));
			    		 }
			    		 else {
			    			 toWrite 
				                     .append("\n")
				                     .append(method.getName())
				                     .append(":\tis abstract");
			    		 }
			 		} catch (IllegalArgumentException e) {
						// invoke
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// invoke
						e.printStackTrace();

					}
		    	 }
			 }
			 else {			// only run the static methods
				 for (Method method : methodsWithRunMe) {
		    		 try {

			    		 if (Modifier.isStatic(method.getModifiers())) {
				    		 System.out.println("lasse Methode " + method.getName() + " laufen");
				    		 method.invoke(object, createParameters(method.getParameterTypes()));
			    		 }
			    		 else {
			    			 toWrite 
				                     .append("\n")
				                     .append(method.getName())
				                     .append(":\tGiven class isn't invocable and method not static.");
			    		 }
			 		} catch (IllegalArgumentException e) {
						// invoke
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// invoke
						e.printStackTrace();

					}
		    	 }
			 }
				 
	    	 
		} catch (InstantiationException e) {
			// TODO newInstance
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// newInstance
			e.printStackTrace();
		}
    	 writeToFile(toWrite.toString());
    }

    private void writeToFile(String stringToWrite) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("report.txt"))) {
            writer.write(stringToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean isInvocable() {
    	int modifiers = clazz.getModifiers();
    	if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
    		 return false;
    	}
    	return true;
    }
    
    private Object[] createParameters(Class[] parameterTypes) {
    	Object[] result = new Object[parameterTypes.length];
    	for (int i=0; i<result.length; i++) {
    		if (parameterTypes[i].isPrimitive()) {
    			switch (parameterTypes[i].getName()) {
    			case "boolean": 
    				result[i] = true;
    				break;
    			case "int": 
    				result[i] = 0;
    				break;
    			case "double": 
    				result[i] = 0.0;
    				break;
    			case "char": 
    				result[i] = 'c';
    				break;
    			case "long": 
    				result[i] = 1;
    				break;
    			case "float": 
    				result[i] = 1.1;
    				break;
    			case "shot": 
    				result[i] = 1;
    				break;
    			case "byte": 
    				result[i] = 1;
    				break;
    			}
    		}
    		else {
    			try {
					result[i] = parameterTypes[i].newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					result[i] = 0;
				}
    		}
    	}
    	return result;
    }
}