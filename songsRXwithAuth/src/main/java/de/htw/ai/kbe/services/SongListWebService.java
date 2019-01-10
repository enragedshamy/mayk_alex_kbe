package de.htw.ai.kbe.services;

import de.htw.ai.kbe.exceptions.WrongSongException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.storage.SongListService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Path("/songLists")
public class SongListWebService {

    @Inject
    public SongListWebService(SongListService songListService) {
        this.songListService = songListService;
    }

    private SongListService songListService;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Set<Song>> getSongListsByUserId(@Context HttpHeaders headers, @QueryParam("userId") String userId) {
        String token = getToken(headers);
        return songListService.getSongListsByUserId(userId, token);
    }

    @GET
    @Path("/{song_list_id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Set<Song> getSongListsById(@Context HttpServletResponse httpResponse,
                                      @Context HttpHeaders headers,
                                      @PathParam("song_list_id") int list_id) throws IOException {
        String token = getToken(headers);
        try {
            return songListService.getSongListsById(list_id, token);
        } catch (ForbiddenException e) {
            httpResponse.sendError(Response.Status.FORBIDDEN.getStatusCode(), "No access for songListId " + list_id);
            return Collections.emptySet();
        } catch (NotFoundException e) {
            httpResponse.sendError(Response.Status.NOT_FOUND.getStatusCode(), "No songList found with songListId " + list_id);
            return Collections.emptySet();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createSong(@Context HttpHeaders headers,
                               @Context UriInfo uriInfo,
                               Set<Song> song) {
        String token = getToken(headers);
        try {
            int newSongListId = songListService.insertSongList(song, token);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(Integer.toString(newSongListId));
            return Response.created(uriBuilder.build()).build();
        } catch (Exception ignored) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Song doesn't exist").build();
        }
    }

    private String getToken(HttpHeaders headers) {
        return headers.getHeaderString(HttpHeaders.AUTHORIZATION);
    }


}