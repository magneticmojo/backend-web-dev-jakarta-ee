package com.example.transactionservlet;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
/**
 * Provides a set of static methods for database operations related to the Guest entity and
 * uses the EntityManager from the {@link DBUtil} class to interact with the database.
 *
 * @author Björn Forsberg
 */
public class GuestDB {

    private GuestDB() {
        throw new AssertionError("Cannot be instantiated");
    }

    /**
     * Inserts a new guest into the database.
     *
     * @param guest The guest to be inserted into the database.
     * @throws RuntimeException if any error occurs during the transaction.
     */
    public static void insert(Guest guest) {
        EntityManager em = DBUtil.getEMFInstance().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(guest);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error while inserting guest", e);
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all guests from the database and sorts them in ascending order of their timestamp.
     *
     * @return A List of all guests from the database.
     * @throws RuntimeException if any error occurs during the transaction.
     */
    public static List<Guest> getAll() {
        try (EntityManager em = DBUtil.getEMFInstance().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Guest> cq = cb.createQuery(Guest.class);
            Root<Guest> rootEntry = cq.from(Guest.class);
            CriteriaQuery<Guest> all = cq.select(rootEntry);
            all.orderBy(cb.asc(rootEntry.get("timestamp")));
            TypedQuery<Guest> allQuery = em.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error while retrieving all guests", e);
        }
    }


}
