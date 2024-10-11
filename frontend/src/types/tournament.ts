export interface Location {
  id: number;
  name: string;
}

export interface Club {
  id: number;
  name: string;
}

export interface Tournament {
  name: string;
  startDateTime: string;
  endDateTime: string;
  location: Location | null;
  maxTeams: number;
  tournamentFormat: string;
  knockoutFormat: string;
  minRank: number;
  maxRank: number;
}

export interface TournamentUpdate {
  name: string;
  startDateTime: string;
  endDateTime: string;
  location: Location;
  prizePool?: number[];
  minRank?: number;
  maxRank?: number;
}