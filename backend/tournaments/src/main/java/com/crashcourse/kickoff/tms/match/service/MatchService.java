package com.crashcourse.kickoff.tms.match.service;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.dto.*;

public interface MatchService {
    public MatchResponseDTO createMatch(Long tournamentId, Long userId, Long parentId);
}
