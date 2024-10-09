package com.crashcourse.kickoff.tms.host;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service("hostProfileSerice")
public class HostProfileServiceImpl implements HostProfileService{
    @PersistenceContext
    private EntityManager entityManager;
    private HostProfileRepository hosts;

    public HostProfileServiceImpl(HostProfileRepository hosts) {
        this.hosts = hosts;
    }
    
    @Transactional
    public HostProfile addHostProfile(User newUser) {
        newUser = entityManager.merge(newUser);

        HostProfile newHostProfile = new HostProfile();
        // Set properties specific to HostProfile
        newHostProfile.setUser(newUser);
        return hosts.save(newHostProfile);
    }
}
