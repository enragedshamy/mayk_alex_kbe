package de.htw.ai.kbe.echo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.kbe.echo.model.Song;
import de.htw.ai.kbe.echo.model.Songs;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.Random;

import static de.htw.ai.kbe.echo.model.Songs.getInstance;

public class SongsServlet extends HttpServlet {

    private Songs songs = null;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.songs = getInstance(getServletContext().getResourceAsStream("/WEB-INF/songs"));
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String responseStr = "\nThis is a SongDB";

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String param = parameterNames.nextElement();
            try {
                if (param.equals("all")) {
                    responseStr = objectMapper.writeValueAsString(songs.getAllSongs());
                    break;
                } else if (param.equals("songId")) {
                    String parameter = request.getParameter(param);
                    int songId = Integer.parseInt(parameter);
                    responseStr = objectMapper.writeValueAsString(songs.getSongById(songId));
                    break;
                }
            } catch (JsonProcessingException e) {
                responseStr = e.toString();
            }
        }

        response.setContentType("application/json");
        response.setStatus(200);
        try (PrintWriter out = response.getWriter()) {
            out.print(responseStr);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectText = "http://localhost:8080/songsServlet?songId=";
        int songId = 0;
        Song songFromRequest = null;

        try {
            songFromRequest = parseSong(request);

            if (songFromRequest.getId() == 0) {
                songId = saveNewSong(songFromRequest);
            } else {
                songId = updateSong(songFromRequest);
            }
            redirectText += songId;
            returnSuccess(response, redirectText);
        } catch (Exception e) {
            returnError(response);
        }

//        writeToFile();
    }

    private void returnError(HttpServletResponse response) throws IOException {
        response.setStatus(400);
        try (PrintWriter out = response.getWriter()) {
            out.print("Wrong Song format!");
        }
    }

    private void returnSuccess(HttpServletResponse response, String redirectText) throws IOException {
        response.setStatus(200);
        try (PrintWriter out = response.getWriter()) {
            out.print(redirectText);
        }
//        response.sendRedirect(redirectText);
    }

    private Song parseSong(HttpServletRequest request) throws Exception {
        ServletInputStream inputStream = request.getInputStream();
        String jsonFromRequest = new String(IOUtils.toByteArray(inputStream));
        return objectMapper.readValue(jsonFromRequest, Song.class);
    }

    private int saveNewSong(Song newSong) {
        int songId = generateNewSongId();
        newSong.setId(songId);
        songs.getAllSongs().add(newSong);
        return songId;
    }

    private int updateSong(Song updatedSong) {
        for (Song tmp : songs.getAllSongs()) {
            if (tmp.getId() == updatedSong.getId()) {
                songs.getAllSongs().remove(tmp);
                break;
            }
        }
        return saveNewSong(updatedSong);
    }

    private int generateNewSongId() {
        return Math.abs(new Random().nextInt());
    }

    protected Songs getSongs() {
        return songs;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected void setSongs(Songs songs) {
        this.songs = songs;
    }

    protected void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected synchronized void writeToFile() throws IOException {
        String contextPath = getServletContext().getRealPath("/");

        String xmlFilePath = contextPath + "WEB-INF\\songs";

        System.out.println(xmlFilePath);

        File myfile = new File(xmlFilePath);

        myfile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(myfile));
        writer.write(objectMapper.writeValueAsString(songs.getAllSongs()));

        writer.close();
    }
}
