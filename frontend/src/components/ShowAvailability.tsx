import React from 'react';
import PlayerProfileCard from './PlayerProfileCard';
import { PlayerAvailabilityDTO } from '../types/playerAvailability';

interface ShowAvailabilityProps {
  availabilities: PlayerAvailabilityDTO[];
  currentUserId: number;
}

export default function ShowAvailability({ availabilities, currentUserId }: ShowAvailabilityProps) {
  const totalPlayers = availabilities.length;
  const availablePlayers = availabilities.filter((a) => a.isAvailable).length;
  const unavailablePlayers = totalPlayers - availablePlayers;

  return (
    <div className="bg-gray-800 rounded-lg p-6 mb-6">
      <h3 className="text-2xl font-semibold mb-4">Club Member's Availability</h3>
      <div className="mb-4">
        <p>Total players: {totalPlayers}</p>
        <p>Available: {availablePlayers}</p>
        <p>Not Available: {unavailablePlayers}</p>
      </div>
      {availabilities.length === 0 ? (
        <p>No player from your club has indicated availability yet.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {availabilities.map((availability) => (
            <PlayerProfileCard
            key={availability.playerId}
            id={availability.playerId}
            name={availability.playerName}
            availability={availability.isAvailable}
            isCurrentUser={availability.playerId === currentUserId}  // This prop tells if the player is the current user
          />
          ))}
        </div>
      )}
    </div>
  );
}
