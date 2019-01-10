package de.htw.ai.kbe.di;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.htw.ai.kbe.model.SongList;
import de.htw.ai.kbe.storage.*;

import org.glassfish.hk2.utilities.binding.AbstractBinder;


public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(Persistence
                .createEntityManagerFactory("songsRXDB-PU"))
                .to(EntityManagerFactory.class);
        bind(SongsServiceJPA.class).to(SongsService.class).in(Singleton.class);
        bind(AuthServiceJPA.class).to(AuthService.class).in(Singleton.class);
        bind(SongListServiceJPA.class).to(SongListService.class).in(Singleton.class);
    }
}