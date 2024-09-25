package com.crashcourse.kickoff.tms.user.service;

import java.util.*;

import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;

public interface PlayerProfileService {
    List<PlayerProfile> getPlayerProfiles();
    PlayerProfile updatePlayerPosition(Long userId, PlayerPosition preferredPosition);
}
