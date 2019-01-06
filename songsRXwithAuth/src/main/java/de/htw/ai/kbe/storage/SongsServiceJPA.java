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
		EntityManager em = emf.createEntityManager();
		List<Song> result = null;
		try {
			TypedQuery<Song> query = em.createQuery("SELECT s FROM Song s", Song.class);
			result = query.getResultList();
		} finally {
			em.close();
		}
		if ( result == null) {
			throw new SongNotFoundException();
		}
		return result;
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
	public synchronized int insertSong(Song song) throws WrongSongException {
		if (isTitleEmpty(song.getTitle())) throw new WrongSongException();
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.persist(song);
			transaction.commit();
			return song.getId();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error adding song: " + e.getMessage());
			transaction.rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString()); // fangen????
		} finally {
			em.close();
		}
	}

	@Override
	public synchronized void deleteSongWithId(Integer id) throws SongNotFoundException {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		Song song = null;
		try {
			song = em.find(Song.class, id);
			if (song != null) {
				System.out.println("Deleting: " + song.getId() + " with title: " + song.getTitle());
				transaction.begin();
				em.remove(song);
				transaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error removing song: " + e.getMessage());
			transaction.rollback();
			throw new PersistenceException("Could not remove entity: " + e.toString());
		} finally {
			em.close();
		}
		if (song == null) {
			throw new SongNotFoundException();
		}
	}

	@Override
	public synchronized void updateSongWithId(Integer id, Song newSong) throws SongNotFoundException, WrongSongException {
		if (isTitleEmpty(newSong.getTitle())) throw new WrongSongException();
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		Song existingSong = null;
		try {
			existingSong = em.find(Song.class, id);
			if (existingSong != null) {
				System.out.println("Updating: " + existingSong.getId() + " with title: " + existingSong.getTitle());
				transaction.begin();
				em.remove(existingSong);
				em.persist(newSong);
				transaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error updating song: " + e.getMessage());
			transaction.rollback();
			throw new PersistenceException("Could not update entity: " + e.toString());
		} finally {
			em.close();
		}
		if (existingSong == null) {
			throw new SongNotFoundException();
		}
	}

	private boolean isTitleEmpty(String title) {
		return title == null || title.isEmpty();
	}

}
