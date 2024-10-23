package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.model.Round;
import com.crashcourse.kickoff.tms.match.dto.*;

public interface MatchService {
    Match createMatch(Long tournamentId);
    List<Round> createBracket(Long tournamentId, int numberOfClubs);
    Match getMatchById(Long id);
    MatchResponseDTO updateMatch(Long id, MatchUpdateDTO matchUpdateDTO);
}
