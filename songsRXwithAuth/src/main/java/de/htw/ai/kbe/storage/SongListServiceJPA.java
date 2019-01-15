package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.exceptions.UserNotFoundException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.model.SongList;
import de.htw.ai.kbe.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SongListServiceJPA implements SongListService {

    private EntityManagerFactory entityManagerFactory;

    @Inject
    public SongListServiceJPA(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<SongList> getAllSongLists() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT a FROM SongList a", SongList.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Set<Song>> getSongListsByUserId(String userId, String token) throws UserNotFoundException {
        User user = getUser(userId);
        Stream<SongList> allSongListsForUser = getAllSongListsForUser(userId);
        if (user.getToken() == null || !user.getToken().equals(token)) {
            allSongListsForUser = getPrivateSongListsForUser(allSongListsForUser);
        }
        return getSongList(allSongListsForUser);
    }

    @Override
    public Set<Song> getSongListsById(int list_id, String token) {
        Optional<SongList> result = getAllSongLists()
                .stream()
                .filter(_songList -> _songList.getId() == list_id)
                .findFirst();
        if (result.isPresent()) {
            SongList songList = result.get();
            if (songList.isPublic() || songList.getUser().getToken() != null && songList.getUser().getToken().equals(token)) {
                return songList.getSongList();
            } else {
                throw new ForbiddenException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public int insertSongList(SongList songList, String token) {
    	Set<Song> newSongs = songList.getSongList();
        existAllSongs(newSongs);
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            SongList newSongList = createSongList(newSongs, token, songList.isPrivate());
            transaction.begin();
            em.persist(newSongList);
            transaction.commit();
            return newSongList.getId();
        } catch (Exception e) {
            System.out.println("Error adding new SongList: " + e.getMessage());
            transaction.rollback();
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteSongListWithId(Integer id, String token) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            SongList songList = em.find(SongList.class, id);
            if (songList != null) {
                canDelete(songList, token);
                transaction.begin();
                em.remove(songList);
                transaction.commit();
            } else {
                throw new NotFoundException();
            }
        } catch (Exception e) {
            transaction.rollback();
            throw new PersistenceException("Could not remove entity: " + e.toString());
        } finally {
            em.close();
        }
    }

    private void canDelete(SongList songList, String token) throws Exception {
        if (songList.getUser().getToken() == null || !songList.getUser().getToken().equals(token))
            throw new Exception();
    }

    private SongList createSongList(Set<Song> newSongs, String token, boolean isPrivate) throws UserNotFoundException {
        SongList songList = new SongList();
        songList.setPrivate(isPrivate);
        songList.setUser(getUserByToken(token));
        songList.setSongList(newSongs);
        return songList;
    }

    private User getUserByToken(String token) throws UserNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT u FROM User u WHERE u.token = '" + token + "'", User.class)
                    .getSingleResult();
        } catch (NoResultException e) {
        	throw new UserNotFoundException();  
        } catch (Exception e) {
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            entityManager.close();
        }
    }

    private void existAllSongs(Set<Song> newSongs) {
        List<Song> allSongs = getAllSongs();
        newSongs.forEach(newSong -> existSong(newSong, allSongs));
    }

    private void existSong(Song newSong, List<Song> allSongs) {
        allSongs
                .stream()
                .filter(song -> match(song, newSong))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    private boolean match(Song song, Song newSong) {
        return song.getId() == newSong.getId() &&
                song.getTitle().equals(newSong.getTitle()) &&
                song.getAlbum().equals(newSong.getAlbum()) &&
                song.getArtist().equals(newSong.getArtist()) &&
                song.getReleased() == song.getReleased();
    }

    private List<Song> getAllSongs() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT s FROM Song s", Song.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            entityManager.close();
        }
    }

    private User getUser(String userId) throws UserNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT u FROM User u WHERE u.userId = '" + userId + "'", User.class)
                    .getSingleResult();
        } catch (NoResultException e) {
        	throw new UserNotFoundException();
        } catch (Exception e) {
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            entityManager.close();
        }
    }

    private Stream<SongList> getAllSongListsForUser(String userId) {
        return getAllSongLists()
                .stream()
                .filter(songList -> songList.getUser().getUserId().equals(userId));
    }

    private Stream<SongList> getPrivateSongListsForUser(Stream<SongList> allSongListsForUser) {
        return allSongListsForUser
                .filter(SongList::isPublic);
    }

    private List<Set<Song>> getSongList(Stream<SongList> allSongListsForUser) {
        return allSongListsForUser
                .map(SongList::getSongList)
                .collect(Collectors.toList());
    }
}
