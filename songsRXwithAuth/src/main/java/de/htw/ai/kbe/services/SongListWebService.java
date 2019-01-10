package de.htw.ai.kbe.services;

import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.storage.SongListService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        return songListService.getSongListsByUserId(userId, token);
    }

    @GET
    @Path("/{song_list_id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Set<Song> getSongListsById(@Context HttpServletResponse httpResponse,
                                      @Context HttpHeaders headers,
                                      @PathParam("song_list_id") int list_id) throws IOException {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
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


}