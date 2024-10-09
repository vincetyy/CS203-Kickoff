import api from './api';
import { PlayerProfile } from '../types/club';
import { PlayerPosition } from '../types/profile';

export const fetchPlayerProfileByUsername = async (username: string): Promise<PlayerProfile> => {
  const response = await api.get(`/playerProfiles/${username}`);
  return response.data;
};

export const updatePlayerProfile = async (playerId: number, preferredPositions: PlayerPosition[], profileDescription: string): Promise<any> => {
  const response = await api.post(`playerProfiles/${playerId}/update`, { preferredPositions, profileDescription });
  return response.data;
};