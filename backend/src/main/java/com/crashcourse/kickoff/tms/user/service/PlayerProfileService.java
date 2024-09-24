package com.crashcourse.kickoff.tms.user.service;

import java.util.*;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;

public interface PlayerProfileService {
    List<PlayerProfile> getPlayers();
    PlayerProfile addPlayer(PlayerProfile player);
    PlayerProfile getPlayer(Long id);  
}
