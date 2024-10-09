export interface Location {
  id: number;
  name: string;
}

export interface Club {
  id: number;
  name: string;
}

export interface Tournament {
  id: number;
  name: string;
  startDateTime: string;
  endDateTime: string;
  location: Location;
  maxTeams: number;
  tournamentFormat: string;
  knockoutFormat: string;
  prizePool: number | null;
  minRank: number | null;
  maxRank: number | null;
  joinedClubs: Club[];
  over: boolean;
  host: object;
}