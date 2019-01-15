package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.exceptions.UserNotFoundException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.model.SongList;

import java.util.List;
import java.util.Set;

public interface SongListService {
    List<SongList> getAllSongLists();

    List<SongList> getSongListsByUserId(String userId, String token) throws UserNotFoundException;

    SongList getSongListById(int list_id, String token);

    int insertSongList(SongList songList, String token);

    void deleteSongListWithId(Integer id, String token);
}
