package de.htw.ai.kbe.services;

import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.storage.AuthService;
import de.htw.ai.kbe.storage.AuthServiceImpl;
import de.htw.ai.kbe.storage.SongsService;
import de.htw.ai.kbe.storage.SongsServiceImpl;

public class AuthWebServiceTest extends JerseyTest {

//	@Override
//	protected Application configure() {
//		return new ResourceConfig(SongsWebService.class);
//	}
	
	private String authToken;
	
	@Override
	protected Application configure() {
		return new ResourceConfig(SongsWebService.class).register(
			new AbstractBinder() {
				@Override
				protected void configure() {
					bind(SongsServiceImpl.class).to(SongsService.class).in(Singleton.class);
			        bind(AuthServiceImpl.class).to(AuthService.class).in(Singleton.class);
				}
			}
		); 
	}
	
	
	
	@Before
	public void authenticateMMuster() {
		authToken = target("/auth").queryParam("userId", "mmuster").request().get(String.class);
		System.out.println(" Token: " +authToken);
	} 
	
	@Test
	public void authenticateNonexistantUserShouldReturn403() {
		Response response = target("/auth").queryParam("userId", "zeus").request().get();
		Assert.assertEquals(403, response.getStatus());
	}
	
	@Test
	public void authenticateExistantUserShouldReturnPlaintext() {
		Response response = target("/auth").queryParam("userId", "fmeier").request().get();
		Assert.assertEquals(MediaType.TEXT_PLAIN, response.getMediaType());
	}
	
	
//----------------------  GET   ------------------------------------------------------------------------
	
/**

	public void getSongWithXMLAcceptHeaderShouldReturnXML() {
		String response = target("/songs/1").request().header(HttpHeaders.AUTHORIZATION, authToken).accept(MediaType.APPLICATION_XML) .get(String.class);
		System.out.println(response);
		Assert.assertTrue(response.startsWith("<?xml"));
	}
	
	@Test
	public void getSongWithJSONAcceptHeaderShouldReturnJSON() {
		String response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println(response);
		Assert.assertTrue(response.startsWith("[{"));
	}

	@Test
	public void getSongWithValidIdShouldReturnSong() {
		Song song = target("/songs/2")
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, authToken)
				.get(Song.class);
		System.out.println(song);
		Assert.assertEquals(2, song.getId());
	}

	@Test
	public void getSongWithNonExistingIdShouldReturn404() {
		Response response = target("/songs/22").request().header(HttpHeaders.AUTHORIZATION, authToken).get();
		Assert.assertEquals(404, response.getStatus());
	}

	@Test
	public void getSongWithStringIdShouldReturn404() {
		Response response = target("/songs/ksksksk").request().header(HttpHeaders.AUTHORIZATION, authToken).get();
		Assert.assertEquals(404, response.getStatus());
	}
	
	public void getSongWithXMLAcceptHeaderAndWithoutAuthTokenShouldReturn401() {
		Response response = target("/songs/1").request().header(HttpHeaders.AUTHORIZATION, authToken).accept(MediaType.APPLICATION_XML).get();
		Assert.assertEquals(401, response.getStatus());
	}
	
	@Test
	public void getSongWithJSONAcceptHeaderAndWithoutAuthTokenShouldReturn401() {
		Response response = target("/songs").request().accept(MediaType.APPLICATION_JSON).get();
		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	public void getSongWithValidIdAndWithoutAuthTokenShouldReturn401() {
		Response response = target("/songs/2")
				.request(MediaType.APPLICATION_JSON).get();				
		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	public void getSongWithNonExistingIdShouldReturn401() {
		Response response = target("/songs/22").request().get();
		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	public void getSongWithStringIdAndWithoutAuthTokenShouldReturn401() {
		Response response = target("/songs/ksksksk").request().get();
		Assert.assertEquals(401, response.getStatus());
	}
	
	
	
	//----------------------  POST   ------------------------------------------------------------------------

	@Test
	public void createSongShouldReturn201AndID() {
		Song song = new Song();
		song.setId(30);
		song.setTitle("Katjuscha");
		song.setReleased(1938);
		Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
		Assert.assertEquals(201, response.getStatus());
		//Assert.assertEquals("3", response.readEntity(String.class));
		Assert.assertTrue(response.getLocation().toString().endsWith("30"));
	}
	
	@Test
	public void createSongWithoutTitleShouldReturn400() {
		Song song = new Song();
		Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
		Assert.assertEquals(400, response.getStatus());
	}
	
	
	
	//----------------------    PUT   ------------------------------------------------------------------------
	
	@Test
	public void updateExistingJSONSongShouldReturn204() {
		Song song = new Song();
		song.setId(29);
		song.setTitle("Die Post");
		Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.json(song));
		Assert.assertEquals(201, response.getStatus());
		song.setArtist("Schubert");
		response = target("/songs/29").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.json(song));
		Assert.assertEquals(204, response.getStatus());
	}
	
	@Test
	public void updateJSONSongWithoutTitleShouldReturn400() {
		Song song = new Song();
		song.setId(30);
		song.setArtist("Matwei Isaakowitsch Blanter");
		song.setReleased(1938);
		Response response = target("/songs/30").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.json(song));
		Assert.assertEquals(400, response.getStatus());
	}
	
	@Test
	public void updateNonexistingJSONSongShouldReturn404() {
		Song song = new Song();
		song.setId(31);
		song.setTitle("Kalinka");
		Response response = target("/songs/31").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.json(song));
		Assert.assertEquals(404, response.getStatus());
	}
	
	@Test
	public void updateExistingXMLSongShouldReturn204() {
		Song song = new Song();
		song.setId(28);
		song.setTitle("Der Leiermann");
		Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
		Assert.assertEquals(201, response.getStatus());
		song.setArtist("Schubert");
		response = target("/songs/28").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.xml(song));
		Assert.assertEquals(204, response.getStatus());
	}
	
	@Test
	public void updateXMLSongWithoutTitleShouldReturn400() {
		Song song = new Song();
		song.setId(30);
		song.setArtist("Matwei Isaakowitsch Blanter");
		song.setReleased(1938);
		Response response = target("/songs/30").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.xml(song));
		Assert.assertEquals(400, response.getStatus());
	}
	
	@Test
	public void updateNonexistingXMLSongShouldReturn404() {
		Song song = new Song();
		song.setId(31);
		song.setTitle("Kalinka");
		Response response = target("/songs/31").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.xml(song));
		Assert.assertEquals(404, response.getStatus());
	}
	
	
	//----------------------  DELETE   ------------------------------------------------------------------------
	
	@Test
	public void deleteNonexistingSongShouldReturn404() {
		Song song = new Song();
		song.setId(31);
		song.setTitle("Kalinka");
		Response response = target("/songs/31").header(HttpHeaders.AUTHORIZATION, authToken).request().delete();
		Assert.assertEquals(404, response.getStatus());
	}
	
	@Test
	public void deleteExistingSongShouldReturn204() {
		Song song = new Song();
		song.setId(27);
		song.setTitle("Auf dem Flusse");
		song.setArtist("Schubert");
		Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
		Assert.assertEquals(201, response.getStatus());
		response = target("/songs/27").request().delete();
		Assert.assertEquals(204, response.getStatus());
	}
	 **/
}
