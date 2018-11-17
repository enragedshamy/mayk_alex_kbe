package de.htw.ai.kbe.echo;

import de.htw.ai.kbe.echo.model.Songs;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static de.htw.ai.kbe.echo.model.Songs.getInstance;

public class SongsServlet extends HttpServlet {

    private Songs songs = null;

    @Override
    public void init() {
        this.songs = getInstance(getServletContext().getResourceAsStream("/WEB-INF/songs"));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String responseStr = "This is a SongDB";
            out.println(responseStr);
        }
    }
}
