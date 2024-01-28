package com.example.formtordbmsservlet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity representation of a guest which each have a unique identifier, name, email, homepage, comment, and timestamp.
 * This class is annotated with JPA annotations to indicate how it should be persisted in a relational database.
 *
 * @author Björn Forsberg
 */
@Entity
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String homepage;
    @Column(length = 1000)
    private String comment;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * Default constructor.
     */
    public Guest() {
    }

    /**
     * Constructs a new Guest with specified properties.
     * @param name the name of the guest
     * @param email the email of the guest
     * @param homepage the homepage of the guest
     * @param comment the comment left by the guest
     * @param timestamp the timestamp when the guest left the comment
     */
    public Guest(String name, String email, String homepage, String comment, Date timestamp) {
        this.name = name;
        this.email = email;
        this.homepage = homepage;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getComment() {
        return comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", homepage='" + homepage + '\'' +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
