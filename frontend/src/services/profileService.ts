import api from './api';
import { PlayerProfile, PlayerPosition } from '../types/profile';

// Set the base URL at the top of the file (from environment variable or a hardcoded value)
const playerProfileBaseURL = import.meta.env.VITE_PLAYER_PROFILE_BASE_URL || 'http://localhost:8081';

// Fetch player profile by username
export const fetchPlayerProfileById = async (id: string): Promise<PlayerProfile> => {
  const response = await api.get(`/playerProfiles/${id}`, {
    baseURL: playerProfileBaseURL // Use the baseURL set at the top
  });
  return response.data;
};

// Update player profile
export const updatePlayerProfile = async (playerId: number, preferredPositions: PlayerPosition[], profileDescription: string): Promise<any> => {
  const response = await api.put(`/playerProfiles/${playerId}/update`, { preferredPositions, profileDescription }, {
    baseURL: playerProfileBaseURL // Use the baseURL set at the top
  });
  return response.data;
};