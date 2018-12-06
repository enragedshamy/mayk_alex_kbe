package de.htw.ai.kbe.model;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.Random;

public class User {
    private int id;
    private String userId;
    private String lastName;
    private String firstName;
    private String token;

    public User() {

    }

    public User(int id, String userId, String lastName, String firstName, String token) {
        this.id = id;
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.token = token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
