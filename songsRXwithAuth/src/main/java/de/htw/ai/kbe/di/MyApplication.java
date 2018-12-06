package de.htw.ai.kbe.di;

import org.glassfish.jersey.server.ResourceConfig;
import de.htw.ai.kbe.di.DependencyBinder;

public class MyApplication extends ResourceConfig {
	
	public MyApplication() {
		packages("de.htw.ai.kbe.di");
		register(new DependencyBinder());
	}
}