package de.htw.ai.kbe.storage;

import java.util.List;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.exceptions.WrongSongException;
import de.htw.ai.kbe.model.Song;

public interface SongsService {

    List<Song> getAllSongs() throws SongNotFoundException;

    Song getSongById(int id) throws SongNotFoundException;

    int insertSong(Song song) throws WrongSongException;

    void deleteSongWithId(Integer id) throws SongNotFoundException;

    void updateSongWithId(Integer id, Song song) throws SongNotFoundException, WrongSongException;

}
