package de.htw.ai.kbe.storage;

import java.util.List;
import java.util.Random;

import de.htw.ai.kbe.exceptions.UserNotFoundException;
import de.htw.ai.kbe.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;


public class AuthServiceJPA implements AuthService {

    private EntityManagerFactory emf;

    @Inject
    public AuthServiceJPA(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public synchronized String generateToken(String userId) throws UserNotFoundException {
        String token = null;
        boolean userFound = false;
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userId = '" + userId + "'", User.class);
            List<User> users = query.getResultList();
            if (!users.isEmpty()) {
                userFound = true;
                token = generateToken();
                transaction.begin();
                for (User user : users) {
                    user.setToken(token);
                    em.persist(user);
                }
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding token to contact: " + e.getMessage());
            transaction.rollback();
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            em.close();
        }
        if (!userFound) {
            throw new UserNotFoundException();
        }
        return token;
    }

    @Override
    public boolean isNotValidToken(String authToken) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<String> query = em.createQuery("SELECT u.token FROM User u WHERE u.token IS NOT NULL", String.class);
            return query.getResultList().stream().noneMatch(token -> token.equals(authToken));
        } finally {
            em.close();
        }
    }


    private String generateToken() {
        return String.valueOf(Math.abs(new Random().nextLong()));
    }

}
