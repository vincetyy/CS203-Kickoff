package com.crashcourse.kickoff.tms.host;

import org.springframework.stereotype.Service;
import java.util.Optional;

import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

import jakarta.transaction.Transactional;

public interface HostProfileService {
    HostProfile addHostProfile(User newUser);
    Optional<HostProfile> getHostProfileByID(Long id);
}
