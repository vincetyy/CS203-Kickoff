package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.*;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.match.dto.MatchUpdateDTO;

public interface SingleEliminationService {
    Bracket createBracket(Long tournamentId, List<Long> joinedClubIds, String jwtToken);
    Match updateMatch(Tournament tournament, Match match, MatchUpdateDTO matchUpdateDTO);
    void seedClubs(Round firstRound, List<Long> clubIds, String jwtToken);
}