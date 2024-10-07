package com.crashcourse.kickoff.tms.player.service;

import java.util.*;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

public interface PlayerProfileService {
    List<PlayerProfile> getPlayerProfiles();
    PlayerProfile updatePlayerPosition(Long userId, PlayerPosition preferredPosition);
    PlayerProfile getPlayerProfile(Long playerId);
    PlayerProfile addPlayerProfile(User newUser, NewUserDTO newUserDTO);
}
