package com.example.transactionservlet;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * This class provides utility for working with the entity manager factory.
 * It provides a singleton access to the EntityManagerFactory for database interactions.
 * This class cannot be instantiated and is only meant to be used statically.
 *
 * @author Björn Forsberg
 */
public class DBUtil {

    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("guestbook");

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an AssertionError if an attempt is made to instantiate it.
     */
    private DBUtil() {
        throw new AssertionError("Cannot be instantiated");
    }

    /**
     * Retrieves the singleton instance of the EntityManagerFactory.
     *
     * @return The EntityManagerFactory instance for the "guestbook" persistence unit
     */
    public static EntityManagerFactory getEMFInstance() {
        return emfInstance;
    }
}
