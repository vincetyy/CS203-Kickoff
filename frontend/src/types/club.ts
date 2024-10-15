export interface Club {
  id: number;
  name: string;
  description?: string;
  players: { id: number }[];
  elo: number;
  captainId: number;
  ratingDeviation: number;
  clubDescription: string;
}

export interface ClubProfile {
  id: number;
  name: string;
  description: string;
  elo: number;
  captainId: number;
  players: number[];
  clubDescription: string;
}