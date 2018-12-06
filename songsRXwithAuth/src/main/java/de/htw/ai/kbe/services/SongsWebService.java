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

// URL fuer diesen Service ist: http://localhost:8080/songsRX/rest/songs 
@Path("/songs")
public class SongsWebService {

    private static boolean authorisation = false;

    @Inject
    public SongsWebService(SongsService songsService, AuthService authService) {
        this.songsService = songsService;
        this.authService = authService;
    }

    private SongsService songsService;
    private AuthService authService;

    //GET http://localhost:8080/songsRX/rest/songs
    //Returns all songs
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Song> getAllSongs(@Context HttpServletResponse httpResponse,
                                  @HeaderParam("Authorization") String authorizationToken) throws IOException {
        if (checkAuthorization(authorizationToken)) {
            httpResponse.sendError(Status.UNAUTHORIZED.getStatusCode(), "Authorization failed");
            return Collections.emptyList();
        }
        System.out.println("getAllSongs: Returning all songs!");
        try {
            return songsService.getAllSongs();
        } catch (SongNotFoundException ignored) {
            httpResponse.sendError(Status.NOT_FOUND.getStatusCode(), "No songs found in DataBase");
        }
        return Collections.emptyList();
    }

    //GET http://localhost:8080/songsRX/rest/songs/1
    //Returns: 200 and contact with id 1
    //Returns: 404 on provided id not found

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Song getSong(@PathParam("id") Integer id,
                        @Context HttpServletResponse httpResponse,
                        @HeaderParam("Authorization") String authorizationToken) throws IOException {
        if (checkAuthorization(authorizationToken)) {
            httpResponse.sendError(Status.UNAUTHORIZED.getStatusCode(), "Authorization failed");
            return null;
        }
        try {
            System.out.println("getSong: Returning song for id " + id);
            return songsService.getSongById(id);
        } catch (SongNotFoundException ignored) {
            System.out.println("getSong: No song found for id " + id);
            httpResponse.sendError(Response.Status.NOT_FOUND.getStatusCode(), "No song found with id " + id);
        }
        return null;
    }


    // POST http://localhost:8080/songsRX/rest/songs with contact in payload
    // Temp. solution returns:
    //  Status Code 201 und URI fuer den neuen Eintrag im http-header 'Location' zurueckschicken, also:
    //  Location: /songsRX/rest/songs/neueID
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createSong(Song song,
                               @Context UriInfo uriInfo,
                               @HeaderParam("Authorization") String authorizationToken) {
        if (checkAuthorization(authorizationToken))
            return Response.status(Status.UNAUTHORIZED).entity("Authorization failed").build();
        try {
            int newSongId = songsService.insertSong(song);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(Integer.toString(newSongId));
            return Response.created(uriBuilder.build()).build();
        } catch (WrongSongException ignored) {
            return Response.status(Status.BAD_REQUEST).entity("Song has no title!").build();
        }
    }

    // was kann schieflaufen?
    // id noch nicht vergeben? Trotzdem Einfuegen oder Fehler zurueck
    // kann was mit song nicht stimmen? Sinnloser Input
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id}")
    public Response updateSong(@PathParam("id") Integer id,
                               Song song,
                               @HeaderParam("Authorization") String authorizationToken) {
        if (checkAuthorization(authorizationToken))
            return Response.status(Status.UNAUTHORIZED).entity("Authorization failed").build();
        try {
            songsService.updateSongWithId(id, song);
            return Response.status(Status.NO_CONTENT).entity("Updating successful").build();
        } catch (SongNotFoundException ignored) {
            return Response.status(Status.NOT_FOUND).entity("No entry with id=" + id + " found.").build();
        } catch (WrongSongException e) {
            return Response.status(Status.BAD_REQUEST).entity("Song has no title!").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id,
                           @HeaderParam("Authorization") String authorizationToken) {
        if (checkAuthorization(authorizationToken))
            return Response.status(Status.UNAUTHORIZED).entity("Authorization failed").build();
        try {
            songsService.deleteSongWithId(id);
            System.out.println("delete: Song deleted for id " + id);
            return Response.status(Status.NO_CONTENT).entity("Deleting successful").build();
        } catch (SongNotFoundException e) {
            System.out.println("delete: Song not found for id " + id);
            return Response.status(Status.NOT_FOUND).entity("No song found with id " + id).build();
        }
    }

    private synchronized boolean isAuthorisation() {
        return authorisation;
    }

    public static synchronized void setAuthorisation() {
        authorisation = true;
    }

    private boolean checkAuthorization(String authorizationToken) {
        if (authorisation)
            return authorizationToken == null || !authService.isTokenValid(authorizationToken);
        return false;
    }
}