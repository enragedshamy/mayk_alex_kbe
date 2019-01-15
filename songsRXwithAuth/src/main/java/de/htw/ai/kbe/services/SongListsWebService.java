package de.htw.ai.kbe.services;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.exceptions.WrongSongException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.storage.AuthService;
import de.htw.ai.kbe.storage.SongsService;

@Path("/songLists1")
public class SongListsWebService {

    @Inject
    public SongListsWebService(SongsService songsService) {
        this.songsService = songsService;
    }

    private SongsService songsService;
    
    //GET http://localhost:8080/songsRX/rest/songLists
    //Returns all songs
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Song> getAllListsSongs(@QueryParam("userId") String userId, @Context HttpServletResponse httpResponse) throws IOException {
        System.out.println("getAllSongLists: Returning all lists from user " + userId +"!");
        try {
            return songsService.getAllSongs();
        } catch (SongNotFoundException ignored) {
            httpResponse.sendError(Status.NOT_FOUND.getStatusCode(), "No songs found in DataBase");
        }
        return Collections.emptyList();
    }
}
