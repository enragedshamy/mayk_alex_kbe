package de.htw.ai.kbe.echo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.kbe.echo.model.Song;
import de.htw.ai.kbe.echo.model.Songs;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static java.util.Collections.singletonList;
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
        responseMock = new MockHttpServletResponse();

        objectMapper = new ObjectMapper();
        underTest.setObjectMapper(objectMapper);

        song = new Song();
        songsList = singletonList(song);
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
    public void doGet_all() throws UnsupportedEncodingException, JsonProcessingException {
        requestMock.addParameter("all");

        underTest.doGet(requestMock, responseMock);

        assertEquals("application/json", responseMock.getContentType());
        assertEquals(200, responseMock.getStatus());

        String actual = responseMock.getContentAsString();
        String expected = objectMapper.writeValueAsString(songs.getAllSongs());

        assertEquals(expected, actual);
    }

    @Test
    public void doGet_byId() throws UnsupportedEncodingException, JsonProcessingException {
        requestMock.addParameter("songId", "1");

        underTest.doGet(requestMock, responseMock);

        assertEquals("application/json", responseMock.getContentType());
        assertEquals(200, responseMock.getStatus());

        String actual = responseMock.getContentAsString();
        String expected = objectMapper.writeValueAsString(songs.getSongById(1));

        assertEquals(expected, actual);
    }
}