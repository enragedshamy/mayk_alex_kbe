package de.htw.ai.kbe.services;

import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.htw.ai.kbe.exceptions.SongNotFoundException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.storage.SongsDAO;

// URL fuer diesen Service ist: http://localhost:8080/songsRX/rest/songs 
@Path("/songs")
public class SongsWebService {
	
	private SongsDAO dao;
	
	@Inject
	public SongsWebService(SongsDAO dao) {
		this.dao = dao;
	}
	
	//GET http://localhost:8080/songsRX/rest/songs
	//Returns all songs
	@GET 
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Collection<Song> getAllSongs() {
		System.out.println("getAllSongs: Returning all songs!");
		// Singletons und Interface vertragen sich nicht so...  .getInstance() gestrichen, geloest via DependencyBinder in()
		return dao.getAllSongs();
		// ? TODO ?:  Wenn Instanziierung schief laueft, oder getAllSongs Fehler wirft, Fehler abfangen und  Response Status zu Errorstatus
	}

	//GET http://localhost:8080/songsRX/rest/songs/1
	//Returns: 200 and contact with id 1
	//Returns: 404 on provided id not found
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Song getSong(@PathParam("id") Integer id, @Context HttpServletResponse httpResponse  ) {
		Song song = dao.getSongById(id);
		if (song != null) {
			System.out.println("getSong: Returning song for id " + id);
			return song;
		} else {
			System.out.println("getSong: No song found for id " + id);
			try {
				httpResponse.sendError(Response.Status.NOT_FOUND.getStatusCode(), "No song found with id " + id);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: nichts zurueckgeben, wenn Fehler auftritt
			}
			return null;
		}
	}
	
	
// POST http://localhost:8080/songsRX/rest/songs with contact in payload
// Temp. solution returns: 
//  Status Code 201 und URI fuer den neuen Eintrag im http-header 'Location' zurueckschicken, also:
//  Location: /songsRX/rest/songs/neueID
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createSong(Song song, @Context UriInfo uriInfo) {
	     int newId = dao.insertSong(song);
	     UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
	     uriBuilder.path(Integer.toString(newId));
	     return Response.created(uriBuilder.build()).build();
	}
	
	@PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/{id}")
    public Response updateSong(@PathParam("id") Integer id, Song song) {
		dao.updateSongWithId(id);
		// was kann schieflaufen? 
		// id noch nicht vergeben? Trotzdem Einfuegen oder Fehler zurueck
		// kann was mit song nicht stimmen? Sinnloser Input 
		return Response.status(Status.NO_CONTENT).entity("Updating successful").build();
    }

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") Integer id) {
		try {
			dao.deleteSongWithId(id);
			System.out.println("delete: Song deleted for id " + id);
//			return Response.ok().build(); Anforderung: 204
			return Response.status(Status.NO_CONTENT).entity("Deleting successful").build();
		}
		catch (SongNotFoundException e) {
			System.out.println("delete: Song not found for id " + id);
			return Response.status(Status.NOT_FOUND).entity("No song found with id " + id).build();
		}
		catch (Exception e) {
			System.out.println("delete: Unexpected server problem by deleting song with id " + id);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unexpected server problem").build();
		}
	}
}