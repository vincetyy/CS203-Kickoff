import React, { useState, useEffect } from 'react';
import { Badge } from './ui/badge'; // Assuming Badge component is a UI element
import { Button } from './ui/button'; // Assuming you have a Button component for the "Manage Player" action
import { fetchPlayerProfileById } from '../services/userService'; // Your service for fetching player data
import { PlayerProfile, PlayerPosition } from '../types/profile';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectUserId, selectIsAdmin } from '../store/userSlice'; // Assuming you have selectIsAdmin in your user slice
import toast from 'react-hot-toast';

interface PlayerProfileCardProps {
  id: number;
  availability: boolean;
  needAvailability: boolean;
}

const PlayerProfileCard: React.FC<PlayerProfileCardProps> = ({ id, availability, needAvailability }) => {
  const navigate = useNavigate();
  const userId = useSelector(selectUserId);
  const isAdmin = useSelector(selectIsAdmin); // Get admin status from Redux
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

  const navigateToProfile = () => {
    if (userId === playerProfile?.user.id) {
      toast.success('That\'s your profile!');
      return;
    } 
    navigate(`/player/${playerProfile?.user.id}`);
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
    <div className="bg-gray-800 rounded-lg p-4 flex items-center space-x-4" onClick={navigateToProfile}>
      <img
        src={`https://picsum.photos/seed/${playerProfile.id + 2000}/100/100`}
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
      {needAvailability && (
        isAdmin ? (
          <Button
            className="bg-blue-500 hover:bg-blue-600 w-20 h-15 flex items-center justify-center"
            onClick={() => console.log('Manage Player clicked')}
          >
            Manage Player
          </Button>
        ) : (
          <Badge
            variant={availability ? 'success' : 'destructive'}
            className="w-20 h-6 flex items-center justify-center whitespace-nowrap"
          >
            {availability ? 'Available' : 'Not Available'}
          </Badge>
        )
      )}
    </div>
  );
};

export default PlayerProfileCard;
