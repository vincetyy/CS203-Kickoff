export interface Tournament {
  id: number;
  name: string;
  startDateTime: string;
  endDateTime: string;
  location: {
    id: number;
    name: string;
  };
  maxTeams: number;
  tournamentFormat: string;
  knockoutFormat: string;
  minRank: number | null;
  maxRank: number | null;
  joinedClubs: any[]; // You might want to define a more specific type for clubs
  over: boolean;
}