export interface Club {
  id: number;
  name: string;
  description?: string;
  players: { id: number }[];
  elo: number;
  ratingDeviation: number;
}

export interface ClubProfile {
  id: number;
  name: string;
  description: string;
  elo: number;
  captainName: string;
  playerNames: string[];
}