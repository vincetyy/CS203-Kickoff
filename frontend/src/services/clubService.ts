import api from './api';
import { Club } from '../types/club';

export const fetchClubs = async (): Promise<Club[]> => {
  const response = await api.get('/clubs');
  return response.data;
};

export const applyToClub = async (clubId: number, playerProfileId: number, desiredPosition: string): Promise<any> => {
  const response = await api.post(`/clubs/${clubId}/apply`, { playerProfileId, desiredPosition });
  return response.data;
};

export const createClub = async (clubData: any): Promise<any> => {
  const response = await api.post('/clubs', clubData);
  return response.data;
};

export const getClubByPlayerId = async (playerId: number): Promise<Club> => {
  const response = await api.get(`/clubs/player/${playerId}`);  // Assuming the URL matches the API endpoint
  return response.data;
};