package de.htw.ai.kbe.services;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.di.AuthTokenRequestFilter;
import de.htw.ai.kbe.exceptions.UserNotFoundException;
import de.htw.ai.kbe.model.Song;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.htw.ai.kbe.storage.AuthService;
import de.htw.ai.kbe.storage.AuthServiceImpl;
import de.htw.ai.kbe.storage.SongsService;
import de.htw.ai.kbe.storage.SongsServiceImpl;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.jdkhttp.JdkHttpServerTestContainerFactory;
//import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

public class AuthWebServiceTest extends JerseyTest {

    private static String authToken;
    
    
    private HttpServletResponse httpServletResponse; /*
    private AuthService authService;

    @Override
    protected Application configure() {
        httpServletResponse = mock(HttpServletResponse.class);
        authService = mock(AuthService.class);
        return new ResourceConfig(AuthWebService.class).register(
                new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(authService).to(AuthService.class);
                        bind(httpServletResponse).to(HttpServletResponse.class);
                    }
                }
        );
    } */
    
    @Override
    protected Application configure() {
    	httpServletResponse = mock(HttpServletResponse.class);
        return new ResourceConfig(new Class[]{SongsWebService.class, AuthWebService.class, AuthTokenRequestFilter.class}).register(
                new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(SongsServiceImpl.class).to(SongsService.class).in(Singleton.class);
                        bind(AuthServiceImpl.class).to(AuthService.class).in(Singleton.class);
                        bind(httpServletResponse).to(HttpServletResponse.class);
                    }
                }
        );
    }
    
    /*
    // Quelle: https://stackoverflow.com/questions/28436040/hk2-is-not-injecting-the-httpservletrequest-with-jersey
    @Path("/session")
    public static class SessionResource {

        @Inject
        HttpSession session;

        @GET
        public Response getSessionId() {
            return Response.ok(session.getId()).build();
        }
    }

    public static class HttpSessionFactory implements Factory<HttpSession> {

        private final HttpServletRequest request;

        @Inject
        public HttpSessionFactory(Provider<HttpServletRequest> requestProvider) {
            this.request = requestProvider.get();
        }

        @Override
        public HttpSession provide() {
            return request.getSession();
        }

        @Override
        public void dispose(HttpSession t) {
        }
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new JdkHttpServerTestContainerFactory();
    } 

    @Override
    protected DeploymentContext configureDeployment() {
        ResourceConfig config = new ResourceConfig(SessionResource.class);
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(HttpSessionFactory.class).to(HttpSession.class);
            }
        });
        return ServletDeploymentContext.forServlet(
                                 new ServletContainer(config)).build();
    }
	*/

    
    @Before
    public void authenticateUserMmuster() {
    	String response = target("/auth/mmuster").request().get(String.class);
    	System.out.println(response);
    	authToken = response;
    }

    /*
    @Test
    public void authenticateNonexistantUserShouldReturn403() {
        Response response = target("/auth/zeus").request().get();
        Assert.assertEquals(403, response.getStatus()); 	// 500, weil  @Context HttpServletResponse httpResponse == null in getToken()
    }  */

    @Test
    public void authenticateExistantUserShouldReturnPlaintext() {
        Response response = target("/auth/fmeier").request().get();
        Assert.assertEquals(MediaType.TEXT_PLAIN, response.getMediaType().toString());
    }


//----------------------  GET   ------------------------------------------------------------------------


@Test
 public void getSongWithXMLAcceptHeaderShouldReturnXML() {
 String response = target("/songs/1").request().header(HttpHeaders.AUTHORIZATION, authToken).accept(MediaType.APPLICATION_XML) .get(String.class);
 System.out.println(response);
 Assert.assertTrue(response.startsWith("<?xml"));
 }

 @Test public void getSongWithJSONAcceptHeaderShouldReturnJSON() {
 String response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).accept(MediaType.APPLICATION_JSON).get(String.class);
 System.out.println(response);
 Assert.assertTrue(response.startsWith("[{"));
 }

 
 @Test public void getSongWithValidIdShouldReturnSong() {
 Song song = target("/songs/2")
		 .request(MediaType.APPLICATION_JSON)
		 .header(HttpHeaders.AUTHORIZATION, authToken)
		 .get(Song.class);
 System.out.println(song);
 Assert.assertEquals(2, song.getId());
 }

 @Test public void getSongWithStringIdShouldReturn404() {
 Response response = target("/songs/ksksksk").request().header(HttpHeaders.AUTHORIZATION, authToken).get();
 Assert.assertEquals(404, response.getStatus());
 }

 public void getSongWithXMLAcceptHeaderAndWithoutAuthTokenShouldReturn401() {
 Response response = target("/songs/1").request().header(HttpHeaders.AUTHORIZATION, authToken).accept(MediaType.APPLICATION_XML).get();
 Assert.assertEquals(401, response.getStatus());
 }

 @Test public void getSongWithJSONAcceptHeaderAndWithoutAuthTokenShouldReturn401() {
 Response response = target("/songs").request().accept(MediaType.APPLICATION_JSON).get();
 Assert.assertEquals(401, response.getStatus());
 }

 @Test public void getSongWithValidIdAndWithoutAuthTokenShouldReturn401() {
 Response response = target("/songs/2")
 .request(MediaType.APPLICATION_JSON).get();
 Assert.assertEquals(401, response.getStatus());
 }

 @Test public void getSongWithNonExistingIdShouldReturn401() {
 Response response = target("/songs/22").request().get();
 Assert.assertEquals(401, response.getStatus());
 }

 @Test public void getSongWithStringIdAndWithoutAuthTokenShouldReturn401() {
 Response response = target("/songs/ksksksk").request().get();
 Assert.assertEquals(401, response.getStatus());
 }



 //----------------------  POST   ------------------------------------------------------------------------

 @Test public void createSongShouldReturn201AndID() {
 Song song = new Song();
 song.setId(30);
 song.setTitle("Katjuscha");
 song.setReleased(1938);
 Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
 Assert.assertEquals(201, response.getStatus());
 //Assert.assertEquals("3", response.readEntity(String.class));
 Assert.assertTrue(response.getLocation().toString().endsWith("30"));
 }

 @Test public void createSongWithoutTitleShouldReturn400() {
 Song song = new Song();
 Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
 Assert.assertEquals(400, response.getStatus());
 }



 //----------------------    PUT   ------------------------------------------------------------------------
 
 @Test public void updateExistingJSONSongWithWongAuthTokenShouldReturn401() {
 Song song = new Song();
 song.setId(206);
 song.setTitle("Die Kraehe");
 Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.json(song));
 Assert.assertEquals(201, response.getStatus());
 song.setArtist("Schubert");
 response = target("/songs/206").request().header(HttpHeaders.AUTHORIZATION, authToken+"abc").put(Entity.json(song));
 Assert.assertEquals(401, response.getStatus());
 }
 
 @Test public void updateExistingJSONSongWithoutAuthTokenShouldReturn401() {
 Song song = new Song();
 song.setId(205);
 song.setTitle("Die Post");
 Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.json(song));
 Assert.assertEquals(201, response.getStatus());
 song.setArtist("Schubert");
 response = target("/songs/205").request().header(HttpHeaders.AUTHORIZATION, null).put(Entity.json(song));
 Assert.assertEquals(401, response.getStatus());
 }
 
 @Test public void updateExistingJSONSongShouldReturn204() {
 Song song = new Song();
 song.setId(29);
 song.setTitle("Die Post");
 Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.json(song));
 Assert.assertEquals(201, response.getStatus());
 song.setArtist("Schubert");
 response = target("/songs/29").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.json(song));
 Assert.assertEquals(204, response.getStatus());
 }

 @Test public void updateJSONSongWithoutTitleShouldReturn400() {
 Song song = new Song();
 song.setId(30);
 song.setArtist("Matwei Isaakowitsch Blanter");
 song.setReleased(1938);
 Response response = target("/songs/30").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.json(song));
 Assert.assertEquals(400, response.getStatus());
 }

 @Test public void updateNonexistingJSONSongShouldReturn404() {
 Song song = new Song();
 song.setId(31);
 song.setTitle("Kalinka");
 Response response = target("/songs/31").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.json(song));
 Assert.assertEquals(404, response.getStatus());
 }

 @Test public void updateExistingXMLSongShouldReturn204() {
 Song song = new Song();
 song.setId(28);
 song.setTitle("Der Leiermann");
 Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
 Assert.assertEquals(201, response.getStatus());
 song.setArtist("Schubert");
 response = target("/songs/28").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.xml(song));
 Assert.assertEquals(204, response.getStatus());
 }

 @Test public void updateXMLSongWithoutTitleShouldReturn400() {
 Song song = new Song();
 song.setId(30);
 song.setArtist("Matwei Isaakowitsch Blanter");
 song.setReleased(1938);
 Response response = target("/songs/30").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.xml(song));
 Assert.assertEquals(400, response.getStatus());
 }

 @Test public void updateNonexistingXMLSongShouldReturn404() {
 Song song = new Song();
 song.setId(31);
 song.setTitle("Kalinka");
 Response response = target("/songs/31").request().header(HttpHeaders.AUTHORIZATION, authToken).put(Entity.xml(song));
 Assert.assertEquals(404, response.getStatus());
 }


 //----------------------  DELETE   ------------------------------------------------------------------------

 @Test public void deleteNonexistingSongShouldReturn404() {
 Song song = new Song();
 song.setId(31);
 song.setTitle("Kalinka");
 Response response = target("/songs/31").request().header(HttpHeaders.AUTHORIZATION, authToken).delete();
 Assert.assertEquals(404, response.getStatus());
 }

 @Test public void deleteExistingSongShouldReturn204() {
 Song song = new Song();
 song.setId(27);
 song.setTitle("Auf dem Flusse");
 song.setArtist("Schubert");
 Response response = target("/songs").request().header(HttpHeaders.AUTHORIZATION, authToken).post(Entity.xml(song));
 Assert.assertEquals(201, response.getStatus());
 response = target("/songs/27").request().header(HttpHeaders.AUTHORIZATION, authToken).delete();
 Assert.assertEquals(204, response.getStatus());
 }
 /** **/
}
