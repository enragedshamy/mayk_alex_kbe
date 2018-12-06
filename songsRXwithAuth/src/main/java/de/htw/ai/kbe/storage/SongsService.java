package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.List;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.services.SongsWebService;

public interface SongsService {
	
	public List<Song> getAllSongs();
	
	public Song getSongById(int id);
	
	public int insertSong(Song song);

	public void deleteSongWithId(Integer id) throws SongNotFoundException;

	public void updateSongWithId(Integer id);
	
	
}
