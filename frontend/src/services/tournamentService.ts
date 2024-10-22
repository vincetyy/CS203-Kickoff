import api from './api';
import { Tournament, TournamentFilter } from '../types/tournament';
import { PlayerAvailabilityDTO, UpdatePlayerAvailabilityDTO } from '../types/playerAvailability';
import { Location } from '../types/tournament';

const tournamentBaseURL = import.meta.env.VITE_TOURNAMENT_SERVICE_BASE_URL || 'http://localhost:8080/api/v1';

// Fetch a specific tournament by its ID
export const fetchTournamentById = async (tournamentId: number): Promise<Tournament> => {
  const response = await api.get(`/tournaments/${tournamentId}`, {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Fetch all tournaments
export const fetchTournaments = async (): Promise<Tournament[]> => {
  const response = await api.get('/tournaments', {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Join a tournament as a club
export const joinTournament = async (clubId: number, tournamentId: number): Promise<any> => {
  const response = await api.post('/tournaments/join', {clubId, tournamentId} , {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Create a new tournament
export const createTournament = async (tournamentData: Partial<Tournament>): Promise<Tournament> => {
  const response = await api.post('/tournaments', tournamentData, {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Update an existing tournament
export const updateTournament = async (tournamentId: number, tournamentData: Partial<Tournament>): Promise<Tournament> => {
  const response = await api.put(`/tournaments/${tournamentId}`, tournamentData, {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Remove a club from a tournament
export const removeClubFromTournament = async (tournamentId: number, clubId: number): Promise<void> => {
  const response = await api.delete(`/tournaments/${tournamentId}/clubs/${clubId}`, {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Fetch player availability for a specific tournament
export const getPlayerAvailability = async (tournamentId: number): Promise<PlayerAvailabilityDTO[]> => {
  const response = await api.get(`/tournaments/${tournamentId}/availability`, {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Update a player's availability for a tournament
export const updatePlayerAvailability = async (data: UpdatePlayerAvailabilityDTO): Promise<void> => {

  const response = await api.put('/tournaments/availability', data, {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};

// Get tournaments by ClubId
export const getTournamentsByClubId = async (id: number, filter: TournamentFilter): Promise<Tournament[]> => {

  const response = await api.get(`/tournaments/${id}/tournaments`, {
    baseURL: tournamentBaseURL,
    params: { filter },
  });
  return response.data;
};

// Get all locations
export const getAllLocations = async (): Promise<Location[]> => {

  const response = await api.get('/locations', {
    baseURL: tournamentBaseURL,
  });
  return response.data;
};
