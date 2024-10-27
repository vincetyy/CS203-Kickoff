package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.model.Round;
import com.crashcourse.kickoff.tms.match.repository.RoundRepository;
import com.crashcourse.kickoff.tms.match.service.MatchService;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private final MatchService matchService;

    @Override
    public Round createRound(int numberOfMatches, int roundNumber) {
        Round round = new Round();
        round.setRoundNumber(Long.valueOf(roundNumber));
        round = roundRepository.save(round);
    
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < numberOfMatches; i++) {
            matches.add(matchService.createMatch(round.getId(), 1L + i));
        }
    
        round.setMatches(matches);
        return roundRepository.save(round);
    }
}
