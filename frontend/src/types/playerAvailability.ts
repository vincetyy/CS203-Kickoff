export interface PlayerAvailabilityDTO {
    playerId: number;
    playerName: string;
    available: boolean;
  }
  
  export interface UpdatePlayerAvailabilityDTO {
    tournamentId: number;
    playerId: number;
    available: boolean;
  }
  