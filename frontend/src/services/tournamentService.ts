import api from './api';
import { Tournament } from '../types/tournament';
import { PlayerAvailabilityDTO, UpdatePlayerAvailabilityDTO } from '../types/playerAvailability';

// Fetch a specific tournament by its ID
export const fetchTournamentById = async (tournamentId: number): Promise<Tournament> => {
  const response = await api.get(`/tournaments/${tournamentId}`);
  return response.data;
};

// Fetch all tournaments
export const fetchTournaments = async (): Promise<Tournament[]> => {
  const response = await api.get('/tournaments');
  return response.data;
};

// Join a tournament as a club
export const joinTournament = async (clubId: number, tournamentId: number): Promise<any> => {
  const response = await api.post('/tournaments/join', { clubId, tournamentId });
  return response.data;
};

// Create a new tournament
export const createTournament = async (tournamentData: Partial<Tournament>): Promise<Tournament> => {
  const response = await api.post('/tournaments', tournamentData);
  return response.data;
};

// Update an existing tournament
export const updateTournament = async (tournamentId: number, tournamentData: Partial<Tournament>): Promise<Tournament> => {
  const response = await api.put(`/tournaments/${tournamentId}`, tournamentData);
  return response.data;
};

// Remove a club from a tournament
export const removeClubFromTournament = async (tournamentId: number, clubId: number): Promise<void> => {
  const response = await api.delete(`/tournaments/${tournamentId}/clubs/${clubId}`);
  return response.data;
};

// Fetch player availability for a specific tournament
export const getPlayerAvailability = async (tournamentId: number): Promise<PlayerAvailabilityDTO[]> => {
  const response = await api.get(`/tournaments/${tournamentId}/availability`);
  return response.data;
};

// Update a player's availability for a tournament
export const updatePlayerAvailability = async (data: UpdatePlayerAvailabilityDTO): Promise<void> => {
  const response = await api.put(`/tournaments/availability`, data);
  return response.data;
};
