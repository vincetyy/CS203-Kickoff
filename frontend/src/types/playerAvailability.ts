export interface PlayerAvailabilityDTO {
    playerId: number;
    clubId: number;
    playerName: string;
    available: boolean;
  }
  
  export interface UpdatePlayerAvailabilityDTO {
    tournamentId: number;
    playerId: number;
    clubId: number;
    available: boolean;
  }
  