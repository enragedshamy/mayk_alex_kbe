package de.htw.ai.kbe.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.model.Song;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Collections.emptyList;

public class SongsServiceImpl implements SongsService {

    private static List<Song> songs;

    public SongsServiceImpl() {
        readSongsFromFile();
    }

    private void readSongsFromFile() {
        try {
            InputStream songsJson = Thread.currentThread().getContextClassLoader().getResourceAsStream("songs");
            songs = new ObjectMapper().readValue(songsJson, new TypeReference<List<Song>>() {
            });
        } catch (IOException e) {
            System.out.println("File songs.json can not be loaded!");
            songs = emptyList();
        }
    }

    @Override
    public synchronized List<Song> getAllSongs() {
        return songs;
    }

    @Override
    public synchronized Song getSongById(int id) {
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
