package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.exceptions.UserNotFoundException;

public interface AuthService {

    String generateToken(String userId) throws UserNotFoundException;

    boolean isNotValidToken(String authToken);
}
