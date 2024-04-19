package com.inqoolApp.tennis;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Interface for all operations on the database.
 *
 * @author Jan Vondrasek
 * @param <T> type of entity
*/


@Repository
public interface GeneralRepository<T> {

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the entity with the specified ID, or null if not found
     */
    T findById(Long id);

    /**
     * Retrieves all entities of the managed type.
     *
     * @return a list of all entities of the managed type
     */
    List<T> findAll();

    /**
     * Saves or updates the given entity.
     *
     * @param entity the entity to save or update
     * @return the saved or updated entity
     */
    T save(T entity);

    /**
     * Deletes an entity by its ID. <>
     *
     * @param id the ID of the entity to delete
     */
    void deleteById(Long id);

    /**
     * Deletes all entities of the managed type.
     */
    void deleteAll();

    /**
     * Retrieves reservations by court ID.
     *
     * @param id the ID of the court to retrieve reservations for
     * @return a list of reservations associated with the specified court ID
     */
    List<T> getReservationsByCourtId(Long id);

    /**
     * Retrieves reservations by phone number.
     *
     * @param phoneNumber the phone number associated with the reservations
     * @return a list of reservations associated with the specified phone number
     */
    List<T> getReservationsByPhoneNumber(String phoneNumber);

    /**
     * Retrieves present reservations by phone number.
     *
     * @param phoneNumber the phone number associated with the reservations
     * @return a list of present reservations associated with the specified phone number
     */
    List<T> getPresentReservationsByPhoneNumber(String phoneNumber);

    /**
     * Retrieves users by phone number.
     *
     * @param phoneNumber the phone number of the users to retrieve
     * @param userClass the class representing the type of users to retrieve
     * @return a list of users with the specified phone number and class
     */
    List<T> getUserByPhoneNumber(String phoneNumber, Class<T> userClass);

    /**
     * Checks reservation time availability.
     *
     * @param id the ID of the court to check reservations for
     * @param start the start time of the reservation
     * @param end the end time of the reservation
     * @return a list of conflicting reservations based on the specified court ID and reservation time
     */
    List<T> checkReservationTime(Long id, LocalDateTime start, LocalDateTime end);
}