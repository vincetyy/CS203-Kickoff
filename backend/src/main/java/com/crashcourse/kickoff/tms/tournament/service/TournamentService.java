package com.crashcourse.kickoff.tms.tournament.service;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.tournament.dto.*;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;

import java.util.List;

public interface TournamentService {

    TournamentResponseDTO createTournament(TournamentCreateDTO tournamentCreateDTO, Long userIdFromToken);

    TournamentResponseDTO getTournamentById(Long id);

    List<TournamentResponseDTO> getAllTournaments();

    TournamentResponseDTO updateTournament(Long id, TournamentCreateDTO tournamentCreateDTO);

    void deleteTournament(Long id);

    TournamentResponseDTO joinTournamentAsClub(TournamentJoinDTO tournamentJoinDTO);

    List<Club> getAllClubsInTournament(Long id);

    List<TournamentResponseDTO> getTournamentsForClub(Long clubId, TournamentFilter filter);

}
