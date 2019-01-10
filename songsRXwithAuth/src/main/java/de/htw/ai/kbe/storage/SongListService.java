package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.model.SongList;

import java.util.List;
import java.util.Set;

public interface SongListService {
    List<SongList> getAllSongLists();

    List<Set<Song>> getSongListsByUserId(String userId, String token);

    Set<Song> getSongListsById(int list_id, String token);

    int insertSongList(Set<Song> song, String token);
}
