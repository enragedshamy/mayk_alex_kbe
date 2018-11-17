package de.htw.ai.kbe.echo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.kbe.echo.model.Song;
import de.htw.ai.kbe.echo.model.Songs;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;

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

    private void writeToFile() throws IOException {
        songs.getAllSongs().add(new Song());

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
