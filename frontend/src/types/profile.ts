// Enums for PlayerPosition
export enum PlayerPosition {
  POSITION_FORWARD = "POSITION_FORWARD",
  POSITION_MIDFIELDER = "POSITION_MIDFIELDER",
  POSITION_DEFENDER = "POSITION_DEFENDER",
  POSITION_GOALKEEPER = "POSITION_GOALKEEPER"
}

// Interface for Club
export interface Club {
  id: number;
  name: string;
  description?: string;
  // Add other Club properties if needed
}

// Interface for User
export interface User {
  id: number;
  username: string;
  // Add other User properties if needed
}

// Interface for PlayerProfile
export interface PlayerProfile {
  id: number;
  username: String;
  preferredPositions: PlayerPosition[];
  profileDescription: string;
}