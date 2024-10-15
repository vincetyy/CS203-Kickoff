package com.crashcourse.kickoff.tms.location.service;

import com.crashcourse.kickoff.tms.location.model.Location;

import java.util.List;

/**
 * Service interface for managing Location entities.
 */
public interface LocationService {

    /**
     * Retrieves a Location by its unique identifier.
     *
     * @param id the unique identifier of the Location.
     * @return the Location entity.
     * @throws jakarta.persistence.EntityNotFoundException if the Location is not found.
     */
    Location getLocationById(Long id);

    /**
     * Retrieves all Locations.
     *
     * @return a list of all Location entities.
     */
    List<Location> getAllLocations();

    /**
     * Creates a new Location.
     *
     * @param location the Location entity to create.
     * @return the created Location entity.
     */
    Location createLocation(Location location);

    /**
     * Updates an existing Location.
     *
     * @param id       the unique identifier of the Location to update.
     * @param location the Location entity containing updated information.
     * @return the updated Location entity.
     * @throws jakarta.persistence.EntityNotFoundException if the Location is not found.
     */
    Location updateLocation(Long id, Location location);

    /**
     * Deletes a Location by its unique identifier.
     *
     * @param id the unique identifier of the Location to delete.
     * @throws jakarta.persistence.EntityNotFoundException if the Location is not found.
     */
    void deleteLocation(Long id);
}
