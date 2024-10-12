export interface Location {
  id: number;
  name: string;
}

export interface Club {
  id: number;
  name: string;
}

export interface HostProfile {
  id: number;
  name: string;
}

export interface Tournament {
  id: number;
  name: string;
  startDateTime: string;
  endDateTime: string;
  location: Location | null;
  prizePool?: number[];
  maxTeams: number;
  tournamentFormat: string;
  knockoutFormat: string;
  minRank: number;
  maxRank: number;
  joinedClubs?: Club[];
  host?: HostProfile;  
}

export interface TournamentUpdate {
  name: string;
  startDateTime: string;
  endDateTime: string;
  location: Location | null;
  prizePool?: number[];
  minRank?: number;
  maxRank?: number;
  joinedClubs?: Club[];
  host?: HostProfile;
}