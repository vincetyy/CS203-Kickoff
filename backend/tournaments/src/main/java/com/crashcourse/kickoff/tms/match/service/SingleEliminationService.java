package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.Bracket;

public interface SingleEliminationService extends BracketService {
    Bracket createBracket(Long tournamentId, int numberOfClubs);
}