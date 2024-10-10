package com.crashcourse.kickoff.tms.host;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;



@Service("hostProfileSerice")
public class HostProfileServiceImpl implements HostProfileService{

    @Autowired
    private HostProfileRepository hostProfileRepository;

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

    @Override
    @Transactional (readOnly = true)
    public Optional<HostProfile> getHostProfileByID(Long id) {
        Optional<HostProfile> hostProfile = hostProfileRepository.findById(id);
        /*
         * ill handle the errors and optional next time, just open an issue vince ty
         */
        return hostProfile;
    }
}
