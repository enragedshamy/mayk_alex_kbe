package de.htw.ai.kbe.services;

import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.storage.SongListService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
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


}