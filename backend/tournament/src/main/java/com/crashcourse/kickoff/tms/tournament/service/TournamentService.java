package com.crashcourse.kickoff.tms.tournament.service;

import java.util.List;

import com.crashcourse.kickoff.tms.tournament.dto.PlayerAvailabilityDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentCreateDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentJoinDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentResponseDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentUpdateDTO;
import com.crashcourse.kickoff.tms.tournament.model.PlayerAvailability;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;

public interface TournamentService {

    TournamentResponseDTO createTournament(TournamentCreateDTO tournamentCreateDTO, Long userIdFromToken);

    TournamentResponseDTO getTournamentById(Long id);

    List<TournamentResponseDTO> getAllTournaments();

    TournamentResponseDTO updateTournament(Long id, TournamentUpdateDTO tournamentUpdateDTO);

    void deleteTournament(Long id);

    TournamentResponseDTO joinTournamentAsClub(TournamentJoinDTO tournamentJoinDTO);

    List<Long> getAllClubsInTournament(Long id);

    void removeClubFromTournament(Long tournamentId, Long clubId);

    boolean isOwnerOfTournament(Long tournamentId, Long profileId);

    List<TournamentResponseDTO> getTournamentsForClub(Long clubId, TournamentFilter filter);

    PlayerAvailability updatePlayerAvailability(PlayerAvailabilityDTO dto);  

    List<PlayerAvailabilityDTO> getPlayerAvailabilityForTournament(Long tournamentId);
}
