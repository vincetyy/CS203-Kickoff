package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.model.Round;
import com.crashcourse.kickoff.tms.match.dto.*;

public interface MatchService {
    public Match createMatch(Long tournamentId);
    public List<Round> createBracket(Long tournamentId, Long numberOfClubs);
    public Match getMatchById(Long id);
    public MatchResponseDTO updateMatch(Long id, MatchUpdateDTO matchUpdateDTO);
}
