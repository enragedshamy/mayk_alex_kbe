package de.htw.ai.kbe.songsServlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.kbe.echo.model.Song;
import de.htw.ai.kbe.echo.model.Songs;
import de.htw.ai.kbe.songsServlet.SongsServlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SongsServletTest {

    @Spy
    private SongsServlet underTest;
    @Mock
    private Songs songs;

    private MockHttpServletRequest requestMock;
    private MockHttpServletResponse responseMock;

    private List<Song> songsList;
    private Song song;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        initMocks(this);
        setUpMocks();
    }

    private void setUpMocks() {
        requestMock = new MockHttpServletRequest();
       // requestMock.
        responseMock = new MockHttpServletResponse();
   

        objectMapper = new ObjectMapper();
        underTest.setObjectMapper(objectMapper);

        song = new Song();
        songsList = new ArrayList<>();
        songsList.add(song);
        underTest.setSongs(songs);
        when(songs.getAllSongs()).thenReturn(songsList);
        when(songs.getSongById(1)).thenReturn(song);
    }

    @Test
    public void initShouldSetSongs() {
        Songs songs = underTest.getSongs();
        assertNotNull(songs);
        List<Song> allSongs = songs.getAllSongs();
        assertNotNull(allSongs);
        assertTrue(!allSongs.isEmpty());
        assertEquals(allSongs.size(), 1);
    }

    @Test
    public void initShouldSetObjectMapper() {
        assertNotNull(underTest.getObjectMapper());
        assertEquals(underTest.getObjectMapper(), objectMapper);
    }

    @Test
    public void doGet_all() throws IOException {
        requestMock.addParameter("all");

        underTest.doGet(requestMock, responseMock);

        assertEquals("application/json", responseMock.getContentType());
        assertEquals(200, responseMock.getStatus());

        String actual = responseMock.getContentAsString();
        String expected = objectMapper.writeValueAsString(songs.getAllSongs());

        assertEquals(expected, actual);
    }
    
    @Test 
    public void doGet_allWithWrongAcceptHeader() throws IOException {
        requestMock.addParameter("all");
        requestMock.addHeader("accept", "text/html");
        underTest.doGet(requestMock, responseMock);
        System.out.println(responseMock.getContentAsString());
        assertEquals(406, responseMock.getStatus());
    }

    @Test
    public void doGet_byId() throws IOException {
        requestMock.addParameter("songId", "1");

        underTest.doGet(requestMock, responseMock);

        assertEquals("application/json", responseMock.getContentType());
        assertEquals(200, responseMock.getStatus());

        String actual = responseMock.getContentAsString();
        String expected = objectMapper.writeValueAsString(songs.getSongById(1));

        assertEquals(expected, actual);
    }

    @Test
    public void doPost_error() throws IOException {
        requestMock.setContent("blablablabla".getBytes());
        underTest.doPost(requestMock, responseMock);
        assertEquals(400, responseMock.getStatus());
        assertEquals("Wrong Song format!", responseMock.getContentAsString());
    }

    @Test
    public void doPost_newSong() throws IOException {
        Song songWithoutId = createSongWithoutId();
        
        requestMock.setContent(toString(songWithoutId).getBytes());
        underTest.doPost(requestMock, responseMock);

        assertEquals(200, responseMock.getStatus());
        assertTrue(responseMock.getContentAsString().contains("http://localhost:8080/songsServlet?songId="));

        Song savedSong = songs.getAllSongs().get(1);

        assertTrue(savedSong.getId() != 0);
        assertEquals(songWithoutId.getAlbum(), savedSong.getAlbum());
        assertEquals(songWithoutId.getArtist(), savedSong.getArtist());
        assertEquals(songWithoutId.getTitle(), savedSong.getTitle());
        assertEquals(songWithoutId.getReleased(), savedSong.getReleased());
    }

    private String toString(Song songWithoutId) throws JsonProcessingException {
        return objectMapper.writeValueAsString(songWithoutId);
    }

    private Song createSongWithoutId() {
        Song songWithoutId = new Song();
        songWithoutId.setAlbum("Album");
        songWithoutId.setArtist("Artist");
        songWithoutId.setReleased(1234);
        songWithoutId.setTitle("Title");
        return songWithoutId;
    }

    @Test
    public void doPost_updateSong() throws IOException {
        Song songWithoutId = createSongWithId();

        requestMock.setContent(toString(songWithoutId).getBytes());
        underTest.doPost(requestMock, responseMock);

        assertEquals(200, responseMock.getStatus());
        assertTrue(responseMock.getContentAsString().contains("http://localhost:8080/songsServlet?songId="));

        Song savedSong = songs.getAllSongs().get(1);

        assertTrue(savedSong.getId() != 0);
        assertTrue(savedSong.getId() != songWithoutId.getId());
        assertEquals(songWithoutId.getAlbum(), savedSong.getAlbum());
        assertEquals(songWithoutId.getArtist(), savedSong.getArtist());
        assertEquals(songWithoutId.getTitle(), savedSong.getTitle());
        assertEquals(songWithoutId.getReleased(), savedSong.getReleased());
    }

    private Song createSongWithId() {
        Song songWithId = createSongWithoutId();
        songWithId.setId(1);
        return songWithId;
    }
}