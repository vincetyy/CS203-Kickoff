package com.crashcourse.kickoff.tms.player.service;

import java.util.*;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;

public interface PlayerProfileService {
    List<PlayerProfile> getPlayerProfiles();
    PlayerProfile updatePlayerPosition(Long userId, PlayerPosition preferredPosition);
}
