import api from './api';
import { Tournament } from '../types/tournament';

export const fetchTournaments = async () => {
  const response = await api.get('/tournaments');
  return response.data;
};

export const joinTournament = async (clubId: number, tournamentId: number) => {
  const response = await api.post('/tournaments/join', { clubId, tournamentId });
  return response.data;
};

export const createTournament = async (tournamentData: Partial<Tournament>) => {
  const response = await api.post('/tournaments', tournamentData);
  return response.data;
};