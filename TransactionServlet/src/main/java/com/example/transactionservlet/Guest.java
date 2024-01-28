package com.example.transactionservlet;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a guest in the guestbook application and contains information about the guest,
 * such as their name, email, homepage, comment, and the timestamp of their visit.
 * It also contains a reference to an Image entity if the guest has uploaded an image.
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
    private String comment;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    /**
     * Default no-args constructor as stipulated by the Javabean convention.
     */
    public Guest() {
    }

    /**
     * Constructs a new Guest object with the given parameters.
     *
     * @param name the name of the guest
     * @param email the email of the guest
     * @param homepage the homepage of the guest
     * @param comment the comment left by the guest
     * @param timestamp the time of the guest's visit
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
