package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.model.SongList;
import de.htw.ai.kbe.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.Collections;
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
    public List<Set<Song>> getSongListsByUserId(String userId, String token) {
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

    private User getUser(String userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT u FROM User u WHERE u.userId = '" + userId + "'", User.class)
                    .getSingleResult();
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
