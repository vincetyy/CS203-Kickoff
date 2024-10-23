package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.dto.*;

public interface MatchService {
    public MatchResponseDTO createMatch(MatchCreateDTO matchCreateDTO);
    public List<List<Match>> createBracket(Long tournamentId, Long numberOfClubs);
    public Match getMatchById(Long id);
    public MatchResponseDTO updateMatch(Long id, MatchUpdateDTO matchUpdateDTO);
}
