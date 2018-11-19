package de.htw.ai.kbe.echo.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Songs {
    private static Songs instance = null;

    private List<Song> songs;

    private Songs() {

    }

    private Songs(InputStream resourceAsStream) {
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

    public synchronized static Songs getInstance(InputStream resourceAsStream) {
        if (instance == null) {
            instance = new Songs(resourceAsStream);
        }
        return instance;
    }
}
