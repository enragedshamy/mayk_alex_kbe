package de.htw.ai.kbe.di;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import de.htw.ai.kbe.storage.SongsDAO;
import de.htw.ai.kbe.storage.SongsDB;

public class DependencyBinder extends AbstractBinder {
	
	@Override
	protected void configure() {
		bind(SongsDB.class).to(SongsDAO.class).in(Singleton.class); 
		}
}