package de.htw.ai.kbe.di;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.htw.ai.kbe.storage.AuthService;
import de.htw.ai.kbe.storage.AuthServiceImpl;
import de.htw.ai.kbe.storage.AuthServiceJPA;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import de.htw.ai.kbe.storage.SongsService;
import de.htw.ai.kbe.storage.SongsServiceImpl;
import de.htw.ai.kbe.storage.SongsServiceJPA;


public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {
    	 bind (Persistence
                 .createEntityManagerFactory("songsRXDB-PU"))
                 .to(EntityManagerFactory.class);
        bind(SongsServiceJPA.class).to(SongsService.class).in(Singleton.class);
        bind(AuthServiceJPA.class).to(AuthService.class).in(Singleton.class);
    }
}