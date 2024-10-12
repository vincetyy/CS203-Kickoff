export interface PlayerAvailabilityDTO {
    playerId: number;
    playerName: string;
    isAvailable: boolean;
  }
  
  export interface UpdatePlayerAvailabilityDTO {
    tournamentId: number;
    playerId: number;
    isAvailable: boolean;
  }
  