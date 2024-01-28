package com.example.formtordbmsservlet;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
/**
 * Utility class for managing the EntityManagerFactory in an application.
 *
 * @author Björn Forsberg
 */
public class DBUtil {

    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("guestbook");

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws AssertionError if this constructor is called from within this class.
     */
    private DBUtil() {
        throw new AssertionError("DBUtil class should not be instantiated.");
    }

    /**
     * Returns the single instance of the EntityManagerFactory that is used throughout the application.
     *
     * @return the EntityManagerFactory instance
     */
    public static EntityManagerFactory getEMFInstance() {
        return emfInstance;
    }
}
