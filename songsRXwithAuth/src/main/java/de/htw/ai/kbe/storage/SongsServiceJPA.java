package de.htw.ai.kbe.storage;

import java.util.List;

import javax.inject.Inject; 
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.exceptions.WrongSongException;
import de.htw.ai.kbe.model.Song;

public class SongsServiceJPA implements SongsService {

	private EntityManagerFactory emf;

	@Inject
	public SongsServiceJPA(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public List<Song> getAllSongs() throws SongNotFoundException {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public Song getSongById(int id) throws SongNotFoundException {
        EntityManager em = emf.createEntityManager();
        Song entity = null;
        try {
            entity = em.find(Song.class, id);
        } finally {
            em.close();
        }
        if ( entity == null) {
        	throw new SongNotFoundException();
        }
        return entity;
	}

	@Override
	public int insertSong(Song song) throws WrongSongException {
		// TODO Automatisch generierter Methodenstub
		return 0;
	}

	@Override
	public void deleteSongWithId(Integer id) throws SongNotFoundException {
		// TODO Automatisch generierter Methodenstub

	}

	@Override
	public void updateSongWithId(Integer id, Song song) throws SongNotFoundException, WrongSongException {
		// TODO Automatisch generierter Methodenstub

	}

}
