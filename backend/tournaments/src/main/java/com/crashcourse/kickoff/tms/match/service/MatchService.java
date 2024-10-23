package com.crashcourse.kickoff.tms.match.service;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.dto.*;

public interface MatchService {
    public MatchResponseDTO createMatch(MatchCreateDTO matchCreateDTO);
    public MatchResponseDTO getMatchById(Long id);
    public MatchResponseDTO updateMatch(Long id, MatchUpdateDTO matchUpdateDTO);
}
