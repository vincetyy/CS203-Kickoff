import api from './api';
import { Club, ClubProfile } from '../types/club';

const clubBaseURL = import.meta.env.VITE_CLUB_SERVICE_BASE_URL || 'http://localhost:8082';

export const fetchClubs = async (): Promise<Club[]> => {
  const response = await api.get('/clubs', {
    baseURL: clubBaseURL,
  });
  return response.data;
};

export const applyToClub = async (clubId: number, playerProfileId: number, desiredPosition: string): Promise<any> => {
  const response = await api.post(`/clubs/${clubId}/apply`, { playerProfileId, desiredPosition }, {
    baseURL: clubBaseURL,
  });
  return response.data;
};

export const createClub = async (clubData: any): Promise<any> => {
  const response = await api.post('/clubs', clubData, {
    baseURL: clubBaseURL,
  });
  return response.data;
};

export const getClubByPlayerId = async (playerId: number): Promise<Club> => {
  const response = await api.get(`/clubs/player/${playerId}`, {
    baseURL: clubBaseURL,
  });
  return response.data;
};

export const getClubProfileById = async (clubId: number): Promise<ClubProfile> => {
  const response = await api.get(`/clubs/${clubId}`, {
    baseURL: clubBaseURL,
  });
  return response.data;
};