package com.crashcourse.kickoff.tms.host.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.host.*;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;


@Service("hostProfileSerice")
public class HostProfileServiceImpl implements HostProfileService{
    @PersistenceContext
    private EntityManager entityManager;

    private HostProfileRepository hostProfileRepository;

    public HostProfileServiceImpl(HostProfileRepository hostProfileRepository) {
        this.hostProfileRepository = hostProfileRepository;
    }

    @Override
    public List<HostProfile> getHostProfiles() {
        return hostProfileRepository.findAll();
    }
    
    @Transactional
    public HostProfile addHostProfile(User newUser) {
        User managedUser = entityManager.merge(newUser);
        HostProfile newHostProfile = new HostProfile();
        // Set properties specific to HostProfile
        newHostProfile.setUser(managedUser);
        return hostProfileRepository.save(newHostProfile);
    }

    @Override
    @Transactional
    public Optional<HostProfile> getHostProfileByID(Long id) {
        Optional<HostProfile> hostProfile = hostProfileRepository.findById(id);
        if (!hostProfile.isPresent()) {
            throw new EntityNotFoundException("HostProfile not found with id: " + id);
        }
        return hostProfile;
    }
}
