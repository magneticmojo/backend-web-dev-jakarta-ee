package com.example.lightweightmongodbservlet;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Manages the database operations like inserting a document and fetching all documents.
 * It uses MongoDB as the database.
 *
 * @author Björn Forsberg
 */
public class DatabaseManager {
    private final MongoDatabase database;

    /**
     * Constructs a new DatabaseManager, initializing a connection to the MongoDB database "mynewdb".
     */
    public DatabaseManager() {
        MongoClient mongoClient = MongoClients.create();
        this.database = mongoClient.getDatabase("mynewdb");
    }

    /**
     * Inserts a new document into the "logs" collection in the MongoDB database.
     *
     * @param document The Document object to be inserted into the database.
     */
    public void insertDocument(Document document) {
        getCollection().insertOne(document);
    }

    /**
     * Retrieves all documents from the "logs" collection in the MongoDB database.
     *
     * @return An iterable of Document objects from the MongoDB database.
     */
    public FindIterable<Document> findAllDocuments() {
        return getCollection().find();
    }

    /**
     * Provides a reference to the "logs" collection in the MongoDB database.
     *
     * @return The MongoCollection object for the "logs" collection in the MongoDB database.
     */
    private MongoCollection<Document> getCollection() {
        return database.getCollection("logs");
    }
}
