package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.exceptions.UserNotFoundException;

import java.util.Random;

public interface AuthService {

    public String generateToken(String userId) throws UserNotFoundException;

    boolean isNotValidToken(String authToken);
}
