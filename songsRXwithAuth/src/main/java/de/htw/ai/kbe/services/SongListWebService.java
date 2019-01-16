package de.htw.ai.kbe.services;

import de.htw.ai.kbe.exceptions.UserNotFoundException;
import de.htw.ai.kbe.model.SongList;
import de.htw.ai.kbe.storage.SongListService;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;

@Path("/songLists")
public class SongListWebService {

    @Inject
    public SongListWebService(SongListService songListService) {
        this.songListService = songListService;
    }

    private SongListService songListService;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<SongList> getSongListsByUserId(@Context HttpServletResponse httpResponse,
    											@Context HttpHeaders headers,
    											@QueryParam("userId") String userId) throws IOException {
        String token = getToken(headers);
        try {
        	return songListService.getSongListsByUserId(userId, token);
        } catch (UserNotFoundException e) {
        	 httpResponse.sendError(Response.Status.NOT_FOUND.getStatusCode(), "No user with userId " + userId);
        	 return emptyList();
        }

    }

    @GET
    @Path("/{song_list_id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SongList getSongListsById(@Context HttpServletResponse httpResponse,
                                      @Context HttpHeaders headers,
                                      @PathParam("song_list_id") int list_id) throws IOException {
        String token = getToken(headers);
        try {
            return songListService.getSongListById(list_id, token);
        } catch (ForbiddenException e) {
            httpResponse.sendError(Response.Status.FORBIDDEN.getStatusCode(), "No access for songListId " + list_id);
            return null;
        } catch (NotFoundException e) {
            httpResponse.sendError(Response.Status.NOT_FOUND.getStatusCode(), "No songList found with songListId " + list_id);
            return null;
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createSong(@Context HttpHeaders headers,
                               @Context UriInfo uriInfo,
                               SongList songList) {
        String token = getToken(headers);
        try {
            int newSongListId = songListService.insertSongList(songList, token);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(Integer.toString(newSongListId));
            return Response.created(uriBuilder.build()).build();
        } catch (Exception ignored) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Song doesn't exist").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Context HttpHeaders headers,
                           @PathParam("id") Integer id) {
        String token = getToken(headers);
        try {
            songListService.deleteSongListWithId(id, token);
            return Response.status(Response.Status.NO_CONTENT).entity("Deleting successful").build();
        } catch (NotFoundException ignored) {
            return Response.status(Response.Status.NOT_FOUND).entity("No songList found with id " + id).build();
        } catch (PersistenceException ignored) {
            return Response.status(Response.Status.FORBIDDEN).entity("Forbidden " + id).build();
        }
    }


    private String getToken(HttpHeaders headers) {
        return headers.getHeaderString(HttpHeaders.AUTHORIZATION);
    }


}