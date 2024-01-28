package com.example.formtordbmsservlet;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Handles interactions with the database for the Guest entity.
 * It uses the Jakarta Persistence API to persist Guest objects and retrieve them.
 *
 * @author Björn Forsberg
 */
public class GuestDB {

    /**
     * Inserts a new Guest object into the database. If an exception occurs, the transaction is rolled back.
     *
     * @param guest The Guest object to be inserted.
     * @throws RuntimeException if there is an error while inserting the guest.
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
     * Retrieves all Guest objects from the database, ordered by their timestamps.
     *
     * @return A List of all Guest objects, sorted in ascending order by their timestamps.
     * @throws RuntimeException if there is an error while retrieving the guests.
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
