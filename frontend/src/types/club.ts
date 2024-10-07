export interface Club {
  id: number;
  name: string;
  description?: string;
  players: { id: number }[];
  elo: number;
  ratingDeviation: number;
}