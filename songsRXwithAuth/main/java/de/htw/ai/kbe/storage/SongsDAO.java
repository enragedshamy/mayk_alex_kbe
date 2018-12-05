package de.htw.ai.kbe.storage;

import java.util.Collection;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.services.SongsWebService;

public interface SongsDAO {
	
	/**
	 * 
	 * @return Collection of Songs, with all storaged songs
	 */
	public Collection<Song> getAllSongs();
	
	/**
	 * 
	 * @param id
	 * @return Song with that id or null if there is no such song
	 */
	public Song getSongById(int id);
	
	/**
	 * 
	 * @param song
	 * @return new generated id
	 */
	public int insertSong(Song song);

	public void deleteSongWithId(Integer id) throws SongNotFoundException;

	public void updateSongWithId(Integer id);
	
	
}
