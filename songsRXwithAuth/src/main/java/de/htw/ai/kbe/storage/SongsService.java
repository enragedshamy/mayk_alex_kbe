package de.htw.ai.kbe.storage;

import java.util.List;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.model.Song;

public interface SongsService {

    public List<Song> getAllSongs() throws SongNotFoundException;

    public Song getSongById(int id) throws SongNotFoundException;

    public int insertSong(Song song);

    public void deleteSongWithId(Integer id) throws SongNotFoundException;

    public void updateSongWithId(Integer id, Song song) throws SongNotFoundException;

}
