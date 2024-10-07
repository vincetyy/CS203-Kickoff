package com.crashcourse.kickoff.tms.host;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

@Service
public class HostProfileService {
    private HostProfileRepository hosts;

    public HostProfileService(HostProfileRepository hosts) {
        this.hosts = hosts;
    }

    public HostProfile addHostProfile(User newUser, NewUserDTO newUserDTO) {
        HostProfile newHostProfile = new HostProfile();
        // Set properties specific to HostProfile
        newHostProfile.setUser(newUser);
        return hosts.save(newHostProfile);
    }
}
