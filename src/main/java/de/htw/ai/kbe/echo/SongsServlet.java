package de.htw.ai.kbe.echo;

import de.htw.ai.kbe.echo.model.Songs;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static de.htw.ai.kbe.echo.model.Songs.getInstance;

public class SongsServlet extends HttpServlet {

    private Songs songs = null;

    @Override
    public void init() {
        this.songs = getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
