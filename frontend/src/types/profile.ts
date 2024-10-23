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
export interface UserPublicDetails {
  id: number;
  username: string;
}

// Interface for PlayerProfile
export interface PlayerProfile {
  id: number;
  username: string;
  preferredPositions: PlayerPosition[];
  profileDescription: string;
}