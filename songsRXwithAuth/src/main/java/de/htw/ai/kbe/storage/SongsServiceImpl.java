package de.htw.ai.kbe.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.commons.Commons;
import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.model.Song;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.Collections.emptyList;

public class SongsServiceImpl implements SongsService {

    private static List<Song> songs;

    public SongsServiceImpl() {
        songs = new Commons<Song>().readSongsFromFile("songs", Song.class);
    }

    @Override
    public synchronized List<Song> getAllSongs() throws SongNotFoundException {
        return Optional.of(songs)
                .orElseThrow(SongNotFoundException::new);
    }

    @Override
    public synchronized Song getSongById(int id) throws SongNotFoundException {
        return songs
                .stream()
                .filter(song -> song.getId() == id)
                .findFirst()
                .orElseThrow(SongNotFoundException::new);
    }


    @Override
    public synchronized int insertSong(Song song) {
        int newSongId = new Random().nextInt();
        song.setId(newSongId);
        songs.add(song);
        return newSongId;
    }

    @Override
    public synchronized void deleteSongWithId(Integer id) throws SongNotFoundException {
        songs.remove(getSongById(id));
    }

    @Override
    public synchronized void updateSongWithId(Integer id, Song newSong) throws SongNotFoundException {
        Song existingSong = getSongById(id);
        songs.remove(existingSong);
        newSong.setId(id);
        songs.add(newSong);
    }
}
