package de.htw.ai.kbe.di;

import javax.inject.Singleton;

import de.htw.ai.kbe.storage.AuthService;
import de.htw.ai.kbe.storage.AuthServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import de.htw.ai.kbe.storage.SongsService;
import de.htw.ai.kbe.storage.SongsServiceImpl;

public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(SongsServiceImpl.class).to(SongsService.class).in(Singleton.class);
        bind(AuthServiceImpl.class).to(AuthService.class).in(Singleton.class);
    }
}