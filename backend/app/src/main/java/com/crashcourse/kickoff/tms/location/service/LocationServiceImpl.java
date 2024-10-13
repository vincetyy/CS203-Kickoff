package com.crashcourse.kickoff.tms.location.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.location.repository.LocationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of the LocationService interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location updateLocation(Long id, Location location) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        existingLocation.setName(location.getName());
        
        return locationRepository.save(existingLocation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location not found with id: " + id);
        }
        locationRepository.deleteById(id);
    }
}
