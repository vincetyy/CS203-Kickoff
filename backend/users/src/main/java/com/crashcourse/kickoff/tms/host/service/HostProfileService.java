package com.crashcourse.kickoff.tms.host.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.host.*;

import jakarta.transaction.Transactional;

public interface HostProfileService {
    List<HostProfile> getHostProfiles();
    HostProfile addHostProfile(User newUser);
    Optional<HostProfile> getHostProfileByID(Long id);
}
