package de.htw.ai.kbe.services;

import de.htw.ai.kbe.di.AuthTokenRequestFilter;
import de.htw.ai.kbe.exceptions.UserNotFoundException;
import de.htw.ai.kbe.storage.AuthService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;

@Path("/auth")
public class AuthWebService {

    @Inject
    public AuthWebService(AuthService authService) {
        this.authService = authService;
    }

    private AuthService authService;

    @GET
    @Path("/{userId}")
    @Produces({MediaType.TEXT_PLAIN})
    public String getSong(@PathParam("userId") String userId, @Context HttpServletResponse httpResponse) throws IOException {
        try {
            String token = authService.generateToken(userId);
            AuthTokenRequestFilter.setAuthorisation();
            return token;
        } catch (UserNotFoundException ignored) {
            httpResponse.sendError(Response.Status.FORBIDDEN.getStatusCode(), "No user found with userId " + userId);
            return null;
        }
    }
}