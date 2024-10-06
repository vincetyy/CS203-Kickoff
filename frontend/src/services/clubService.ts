import api from './api';

export const fetchClubs = async () => {
  const response = await api.get('/clubs');
  return response.data;
};

export const applyToClub = async (clubId: number, playerProfileId: number, desiredPosition: string) => {
  const response = await api.post(`/clubs/${clubId}/apply`, { playerProfileId, desiredPosition });
  return response.data;
};