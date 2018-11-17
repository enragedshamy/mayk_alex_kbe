package de.htw.ai.kbe.echo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.kbe.echo.model.Songs;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
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
        try (PrintWriter out = response.getWriter()) {
            out.print(responseStr);
        } catch (Exception ignored) {
        }
    }

    protected Songs getSongs() {
        return songs;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setSongs(Songs songs) {
        this.songs = songs;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
