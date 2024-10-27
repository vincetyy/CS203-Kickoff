package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.match.model.*;
import com.crashcourse.kickoff.tms.match.dto.MatchUpdateDTO;
import com.crashcourse.kickoff.tms.match.repository.SingleEliminationBracketRepository;
import com.crashcourse.kickoff.tms.match.repository.MatchRepository;
import com.crashcourse.kickoff.tms.match.repository.RoundRepository;

import com.crashcourse.kickoff.tms.club.ClubProfile;

import com.crashcourse.kickoff.tms.tournament.model.*;
import com.crashcourse.kickoff.tms.tournament.repository.TournamentRepository;

import com.crashcourse.kickoff.tms.client.ClubServiceClient;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SingleEliminationServiceImpl implements SingleEliminationService {

    private final TournamentRepository tournamentRepository;
    private final SingleEliminationBracketRepository bracketRepository;
    private final RoundRepository roundRepository;
    private final MatchRepository matchRepository;
    private final RoundService roundService;
    private final ClubServiceClient clubServiceClient;

    @Override
    public Bracket createBracket(Long tournamentId, List<Long> joinedClubIds, String jwtToken) {
        /*
         * Validation
         */
        int numberOfClubs = joinedClubIds.size();
        if (numberOfClubs == 0) {
            throw new EntityNotFoundException("No clubs found");
        }
        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));
        
        /*
         * Create Bracket
         */
        int numberOfRounds = (int) Math.ceil(Math.log(numberOfClubs) / Math.log(2));
        SingleEliminationBracket bracket = new SingleEliminationBracket();
        List<Round> bracketRounds = new ArrayList<>();

        while (numberOfRounds > 0) {
            int size = (int) Math.pow(2, numberOfRounds - 1);
            bracketRounds.add(roundService.createRound(size, 1L + numberOfRounds));
            numberOfRounds--;
        }

        bracket.setRounds(bracketRounds);
        seedClubs(bracketRounds.get(0), joinedClubIds, jwtToken);
        bracketRepository.save(bracket);        

        bracket.setTournament(tournament);
        tournament.setBracket(bracket);

        return bracket;
    }

    /*
     * Seeding Algorithm
     */
    private List<Integer> generateStandardSeedOrder(int bracketSize) {
        if ((bracketSize & (bracketSize - 1)) != 0) {
            throw new IllegalArgumentException("Bracket size must be a power of 2");
        }
        return generateSeedsRecursively(bracketSize);
    }

    private List<Integer> generateSeedsRecursively(int bracketSize) {
        if (bracketSize == 1) {
            List<Integer> seed = new ArrayList<>();
            seed.add(1);
            return seed;
        }

        List<Integer> prevSeeds = generateSeedsRecursively(bracketSize / 2);
        List<Integer> mirroredSeeds = new ArrayList<>();

        for (int seed : prevSeeds) {
            mirroredSeeds.add(bracketSize + 1 - seed);
        }

        List<Integer> combinedSeeds = new ArrayList<>();
        for (int i = 0; i < prevSeeds.size(); i++) {
            combinedSeeds.add(prevSeeds.get(i));
            combinedSeeds.add(mirroredSeeds.get(i));
        }

        return combinedSeeds;
    }

    @Override
    public void seedClubs(Round firstRound, List<Long> clubIds, String jwtToken) {
        // Fetch club profiles
        List<ClubProfile> clubs = new ArrayList<>();
        for (Long id : clubIds) {
            ClubProfile club = clubServiceClient.getClubProfileById(id, jwtToken);
            if (club != null) {
                clubs.add(club);
            }
        }

        clubs.sort(Comparator.comparingDouble(ClubProfile::getElo).reversed());
        System.out.println(clubs);

        int numberOfClubs = clubs.size();
        int bracketSize = (int) Math.pow(2, Math.ceil(Math.log(numberOfClubs) / Math.log(2)));
        int byes = bracketSize - numberOfClubs;

        List<Match> matches = firstRound.getMatches();
        int totalMatches = matches.size();

        List<Integer> seedPositions = generateStandardSeedOrder(bracketSize);
        if (totalMatches * 2 < seedPositions.size()) {
            throw new RuntimeException("Not enough matches to seed all clubs.");
        }

        int seedIndex = 0;

        for (Match match : matches) {
            if (seedIndex < seedPositions.size()) {
                int seed1 = seedPositions.get(seedIndex);
                if (seed1 <= numberOfClubs) {
                    Long club1Id = clubs.get(seed1 - 1).getId(); // seeds are 1-based
                    match.setClub1Id(club1Id);
                } else {
                    if (byes > 0) {
                        match.setClub1Id(null); // Bye
                        byes--;
                    }
                }
                seedIndex++;
            }

            if (seedIndex < seedPositions.size()) {
                int seed2 = seedPositions.get(seedIndex);
                if (seed2 <= numberOfClubs) {
                    Long club2Id = clubs.get(seed2 - 1).getId(); // seeds are 1-based
                    match.setClub2Id(club2Id);
                } else {
                    if (byes > 0) {
                        match.setClub2Id(null); // Bye
                        byes--;
                    }
                }
                seedIndex++;
            }

            matchRepository.save(match);
        }

        roundRepository.save(firstRound);
    }

    @Override
    public Match updateMatch(Tournament tournament, Match match, MatchUpdateDTO matchUpdateDTO) {
        /*
         * Validation for Tournament and Match handled in TournamentService
         */
        if (!(tournament.getBracket() instanceof SingleEliminationBracket singleEliminationBracket)) {
            throw new RuntimeException("Wrong bracket format.");
        }

        Long matchNumber = match.getMatchNumber();
        Long club1Id = matchUpdateDTO.getClub1Id();
        Long club2Id = matchUpdateDTO.getClub2Id();
        Long winningClubId = matchUpdateDTO.getWinningClubId();

        /*
         * If over, send winner to next round
         */
        if (matchUpdateDTO.isOver()) {

            /*
             * Validation for No Clubs: if seeding is done correctly, there should be
             * no empty matches since every preceding match will have at least 1 club
             */
            if (club1Id == null && club2Id == null) {
                throw new RuntimeException("No clubs in match.");
            }

            match.setOver(true);
            
            /*
             * Note that roundNumber counts downwards 
             * so we know if its the last round
             */
            if (match.getRound().getRoundNumber() == 1) {
                singleEliminationBracket.setWinningClubId(winningClubId);
                bracketRepository.save(singleEliminationBracket);
                tournament.setOver(true);
                tournamentRepository.save(tournament);

            } else {
                List<Round> rounds = singleEliminationBracket.getRounds();
                /*
                 * -1 for next round, -1 since we use 1 index
                 */
                Long roundNumber = match.getRound().getRoundNumber();
                Round nextRound = roundRepository.findRoundByRoundNumber(roundNumber - 1);
                List<Match> matches = nextRound.getMatches();

                /*
                 * -1 to account for 1 indexing again
                 */
                Match nextMatch = matches.get((int)Math.ceil(matchNumber/2.0) - 1);
                if (matchNumber % 2 == 1) {
                    nextMatch.setClub1Id(winningClubId);
                } else {
                    nextMatch.setClub2Id(winningClubId);
                }
                matchRepository.save(nextMatch);
            }
        }
        
        return matchRepository.save(match);
    }

}
