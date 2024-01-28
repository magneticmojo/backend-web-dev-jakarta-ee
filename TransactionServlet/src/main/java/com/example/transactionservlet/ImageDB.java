package com.example.transactionservlet;

import jakarta.persistence.EntityManager;

/**
 * Provides the database operations for the Image entity.
 * It uses the EntityManager from the {@link DBUtil} class to interact with the database.
 *
 * @author Björn Forsberg
 */
public class ImageDB {

    private ImageDB() {
        throw new AssertionError("Cannot be instantiated");
    }

    /**
     * Retrieves an Image entity from the database with the given ID.
     *
     * @param id the ID of the image to retrieve
     * @return the Image entity with the given ID or null if no such entity exists
     */
    public static Image getById(Long id) {
        try (EntityManager em = DBUtil.getEMFInstance().createEntityManager()) {
            return em.find(Image.class, id);
        }
    }
}
