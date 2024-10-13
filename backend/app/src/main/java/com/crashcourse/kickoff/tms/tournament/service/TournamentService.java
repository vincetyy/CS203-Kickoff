package com.crashcourse.kickoff.tms.tournament.service;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.tournament.dto.*;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;

import java.util.List;

public interface TournamentService {

    TournamentResponseDTO createTournament(TournamentCreateDTO tournamentCreateDTO, Long userIdFromToken);

    TournamentResponseDTO getTournamentById(Long id);

    List<TournamentResponseDTO> getAllTournaments();

    TournamentResponseDTO updateTournament(Long id, TournamentUpdateDTO tournamentUpdateDTO);

    void deleteTournament(Long id);

    TournamentResponseDTO joinTournamentAsClub(TournamentJoinDTO tournamentJoinDTO);

    List<Club> getAllClubsInTournament(Long id);

    void removeClubFromTournament(Long tournamentId, Long clubId);

    boolean isOwnerOfTournament(Long tournamentId, Long profileId);

    List<TournamentResponseDTO> getTournamentsForClub(Long clubId, TournamentFilter filter);

    void updatePlayerAvailability(Long tournamentId, Long playerId, boolean available);  

    List<PlayerAvailabilityDTO> getPlayerAvailabilityForTournament(Long tournamentId);
}
