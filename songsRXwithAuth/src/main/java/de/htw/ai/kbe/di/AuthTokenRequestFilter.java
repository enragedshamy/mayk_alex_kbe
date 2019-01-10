package de.htw.ai.kbe.di;

import de.htw.ai.kbe.storage.AuthService;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class AuthTokenRequestFilter implements ContainerRequestFilter {

    private static boolean authorisation = false;

    private AuthService authService;

    @Inject
    public AuthTokenRequestFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        if (!containerRequestContext.getUriInfo().getPath().contains("auth"))
            if (isAuthorisation()) {
                String authToken = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
                if (authToken == null || authService.isNotValidToken(authToken))
                    throw new NotAuthorizedException("Not Authorized");

            }
    }

    private synchronized boolean isAuthorisation() {
        return authorisation;
    }

    public static synchronized void setAuthorisation() {
        authorisation = true;
    }
}
