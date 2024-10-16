import React, { useState, useEffect } from 'react';
import { Badge } from './ui/badge'; // Assuming Badge component is a UI element
import { fetchPlayerProfileById } from '../services/profileService'; // Your service for fetching player data
import { PlayerProfile, PlayerPosition } from '../types/profile';

interface PlayerProfileCardProps {
  id: number;
  availability: boolean;
}

const PlayerProfileCard: React.FC<PlayerProfileCardProps> = ({ id, availability }) => {
  const [playerProfile, setPlayerProfile] = useState<PlayerProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Fetch the player profile data on component mount
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const profile = await fetchPlayerProfileById(String(id)); // Fetch profile using the player ID
        setPlayerProfile(profile);
      } catch (err) {
        setError('Failed to load player profile');
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, [id]);

  // Helper to format the position string
  const formatPosition = (position?: PlayerPosition) => {
    if (!position) return 'No position specified';
    return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
  };

  // Conditional rendering for loading, error, and profile display
  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  if (!playerProfile) {
    return <div>No profile data available</div>;
  }

  return (
    <div className="bg-gray-800 rounded-lg p-4 flex items-center space-x-4 w-96">
      <img
        src={`https://picsum.photos/seed/${playerProfile.id+2000}/100/100`}
        alt={`${playerProfile.user.username}'s profile`}
        className="w-16 h-16 rounded-full object-cover"
      />
      <div className="flex-grow">
        <h3 className="text-lg font-semibold">{playerProfile.user.username}</h3>
        <p className="text-sm text-gray-400">
          {playerProfile.preferredPositions.length > 0
            ? playerProfile.preferredPositions.map((position) => formatPosition(position)).join(', ')
            : 'No position specified'}
        </p>
      </div>
      <Badge
        variant={availability ? 'success' : 'destructive'}
        className="w-24 justify-center whitespace-nowrap"
      >
        {availability ? 'Available' : 'Not Available'}
      </Badge>
    </div>
  );
};

export default PlayerProfileCard;
