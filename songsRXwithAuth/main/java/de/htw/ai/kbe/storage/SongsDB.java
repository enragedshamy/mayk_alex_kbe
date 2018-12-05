package de.htw.ai.kbe.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.model.Song;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SongsDB implements SongsDAO {
	
	
    private List<Song> songs;
    
    
    private SongsDB(InputStream resourceAsStream) {
        this.songs = new ArrayList<>();
        readSongsFromFile(resourceAsStream);
    }

    private void readSongsFromFile(InputStream resourceAsStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("songs");
        TypeReference<List<Song>> typeReference = new TypeReference<List<Song>>() {
        };
        try {
            songs = objectMapper.readValue(resourceAsStream, typeReference);
        } catch (IOException e) {
            System.out.println("File songs.json can not be loaded!");
            System.exit(1);
        }
    }

    public List<Song> getAllSongs() {
        return songs;
    }

    public Song getSongById(int id) {
        for (Song song : songs) {
            if (song.getId() == id)
                return song;
        }
        return null;
    }


	@Override
	public synchronized int insertSong(Song song) {
		// TODO Automatisch generierter Methodenstub
		return 0;
	}

	@Override
	public synchronized void deleteSongWithId(Integer id) throws SongNotFoundException {
		// TODO Automatisch generierter Methodenstub
		
	}

	@Override
	public synchronized void updateSongWithId(Integer id) {
		// TODO Automatisch generierter Methodenstub
		
	}
}
