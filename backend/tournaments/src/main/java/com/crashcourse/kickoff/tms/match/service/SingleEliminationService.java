package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.Bracket;
import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.match.dto.MatchUpdateDTO;

public interface SingleEliminationService extends BracketService {
    Bracket createBracket(Long tournamentId, int numberOfClubs);
    Match updateMatch(Tournament tournament, Match match, MatchUpdateDTO matchUpdateDTO);
    // Bracket seedClubs(Long tournamentId, List<Long> clubIds);
}