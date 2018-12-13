package de.htw.ai.kbe.storage;

import de.htw.ai.kbe.commons.Commons;
import de.htw.ai.kbe.exceptions.UserNotFoundException;
import de.htw.ai.kbe.model.Song;
import de.htw.ai.kbe.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AuthServiceImpl implements AuthService {
    private static List<User> users;

    public AuthServiceImpl() {
        users = new Commons<User>().readSongsFromFile("users", User.class);
    }

    @Override
    public synchronized String generateToken(String userId) throws UserNotFoundException {
        User tmp = getUser(userId);
        String token = generateToken();
        tmp.setToken(token);
        return token;
    }

    @Override
    public boolean isNotValidToken(String authToken) {
        return users
                .stream()
                .noneMatch(user -> user.getToken() != null && user.getToken().equals(authToken));
    }

    private synchronized User getUser(String userId) throws UserNotFoundException {
        return users
                .stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);
    }

    private String generateToken() {
        return String.valueOf(new Random().nextLong());
    }
}
