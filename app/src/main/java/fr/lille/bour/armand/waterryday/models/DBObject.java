package fr.lille.bour.armand.waterryday.models;

/**
 * Interface representing an object that can be stored in a database.
 *
 * @author Armand (Tydax) BOUR
 */

public interface DBObject {

    /**
     * Gets the id used to store the object in the database.
     * @return The id.
     */
    long getId();

    /**
     * Sets the id of the object.
     * @param id The new id of the object.
     */
    void setId(final long id);
}
