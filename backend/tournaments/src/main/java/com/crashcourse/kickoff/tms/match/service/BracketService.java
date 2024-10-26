package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.Bracket;

/*
 * Abstract class since bracket differs by format
 */
public interface BracketService {
    Bracket createBracket(Long tournamentId, int numberOfClubs);
    // Bracket seedClubs(Long tournamentId, List<Long> clubIds);
    // Long getBracketWinnerId(Long tournamentId);
}
