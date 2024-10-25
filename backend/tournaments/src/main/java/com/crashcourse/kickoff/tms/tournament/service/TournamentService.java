package com.crashcourse.kickoff.tms.tournament.service;

import java.util.List;

import com.crashcourse.kickoff.tms.tournament.dto.*;
import com.crashcourse.kickoff.tms.tournament.model.PlayerAvailability;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;
import com.crashcourse.kickoff.tms.match.model.Bracket;

public interface TournamentService {

    TournamentResponseDTO createTournament(TournamentCreateDTO tournamentCreateDTO, Long userIdFromToken);

    TournamentResponseDTO getTournamentById(Long id);

    List<TournamentResponseDTO> getAllTournaments();

    TournamentResponseDTO updateTournament(Long id, TournamentUpdateDTO tournamentUpdateDTO);

    TournamentResponseDTO startTournament(Long id);

    void deleteTournament(Long id);

    TournamentResponseDTO joinTournamentAsClub(TournamentJoinDTO tournamentJoinDTO, String token);

    List<Long> getAllClubsInTournament(Long id);

    void removeClubFromTournament(Long tournamentId, Long clubId);

    boolean isOwnerOfTournament(Long tournamentId, Long profileId);

    List<TournamentResponseDTO> getTournamentsForClub(Long clubId, TournamentFilter filter);

    PlayerAvailability updatePlayerAvailability(PlayerAvailabilityDTO dto);  

    List<PlayerAvailabilityDTO> getPlayerAvailabilityForTournament(Long tournamentId);

    List<Tournament> getHostedTournaments(Long host);
}
