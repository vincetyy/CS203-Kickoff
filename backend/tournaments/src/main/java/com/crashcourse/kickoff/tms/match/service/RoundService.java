package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.model.Round;
import com.crashcourse.kickoff.tms.match.dto.*;

public interface RoundService {
    Round createRound(int numberOfMatches, Long roundNumber);
}
