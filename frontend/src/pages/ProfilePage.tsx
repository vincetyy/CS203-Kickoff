import { useState, useEffect } from 'react';
import axios from 'axios';
import { Input } from '../components/ui/input';
import { Button } from '../components/ui/button';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';

import { Toaster, toast } from 'react-hot-toast';

// Enums for PlayerPosition
enum PlayerPosition {
  POSITION_FORWARD = "POSITION_FORWARD",
  POSITION_MIDFIELDER = "POSITION_MIDFIELDER",
  POSITION_DEFENDER = "POSITION_DEFENDER",
  POSITION_GOALKEEPER = "POSITION_GOALKEEPER"
}

// Interface for Club
interface Club {
  id: number;
  name: string;
  description?: string;
  // Add other Club properties if needed
}

// Interface for User
interface User {
  id: number;
  username: string;
  // Add other User properties if needed
}

// Interface for PlayerProfile
interface PlayerProfile {
  id: number;
  club: Club | null;
  user: User;
  preferredPositions: PlayerPosition[];
  profileDescription: string;
}

export default function PlayerProfilePage() {
  const [playerProfile, setPlayerProfile] = useState<PlayerProfile | null>(null);
  const [preferredPositions, setPreferredPositions] = useState<PlayerPosition[]>([]);
  const [profileDescription, setProfileDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const username = localStorage.getItem('username');

  // Fetch the player profile on component mount
  useEffect(() => {
    if (!username) {
      setError('User not logged in');
      setLoading(false);
      return;
    }

    const fetchPlayerProfile = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/playerProfiles/${username}`);
        console.log("response from server: ", response.data);
        setPlayerProfile(response.data);
        setPreferredPositions(response.data.preferredPositions || []);
        setProfileDescription(response.data.profileDescription || '');
        setLoading(false);
      } catch (err) {
        console.error('Error fetching player profile:', err);
        // setError('Failed to fetch player profile');
        setLoading(false);
      }
    };

    fetchPlayerProfile();
    console.log("player profile after fetch:" , playerProfile);
  }, [username]);

  // Handle updating the preferred positions
  const handlePreferredPositionsChange = (position: PlayerPosition) => {
    setPreferredPositions((prevPositions) =>
      prevPositions.includes(position)
        ? prevPositions.filter((pos) => pos !== position)
        : [...prevPositions, position]
    );
  };

  // Handle submitting the updated profile
  const handleSubmit = async () => {
    if (!playerProfile) return;

    try {
      await axios.put(`http://localhost:8080/playerProfiles/${playerProfile.id}/update`, {
        preferredPositions,
        profileDescription,
      });
      toast.success('Profile updated successfully', {
        duration: 3000,
        position: 'top-center',
      });
    } catch (err) {
      console.error('Error updating profile:', err);
      toast.error('Failed to update profile', {
        duration: 4000,
        position: 'top-center',
      });
    }
  };

  // Utility function to format position names
  const formatPosition = (position: string) => {
    return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
  };

  if (loading) return <div>Loading...</div>;
  if (error || !playerProfile) return <div>Error: {error || 'Profile not found'}</div>;

  return (
    <>
      <div className="p-6 bg-gray-900 rounded-lg">
        <h1 className="text-2xl font-bold mb-4">Player Profile</h1>

        {/* Display user information */}
        <div className="mb-6">
          <h2 className="text-xl font-semibold">User Information</h2>
          <p>Username: {playerProfile.user.username}</p>
          {/* Include other user fields as necessary */}
        </div>

        {/* Display club information */}
        <div className="mb-6">
          <h2 className="text-xl font-semibold">Club Information</h2>
          {playerProfile.club ? (
            <>
              <p>Club Name: {playerProfile.club.name}</p>
              {/* Include other club fields as necessary */}
            </>
          ) : (
            <p>You are not currently associated with a club.</p>
          )}
        </div>

        {/* Editable profile description */}
        <div className="mb-6">
          <h2 className="text-xl font-semibold">Profile Description</h2>
          <Input
            value={profileDescription}
            onChange={(e) => setProfileDescription(e.target.value)}
            placeholder="Describe yourself"
            className="w-full bg-gray-800 border-gray-700"
          />
        </div>

        {/* Preferred positions selection */}
        <div className="mb-6">
          <h2 className="text-xl font-semibold">Preferred Positions</h2>
          <div className="flex flex-wrap">
            {Object.values(PlayerPosition).map((position) => (
              <label key={position} className="mr-4 mb-2 flex items-center">
                <input
                  type="checkbox"
                  checked={preferredPositions.includes(position)}
                  onChange={() => handlePreferredPositionsChange(position)}
                  className="form-checkbox h-4 w-4 text-blue-600"
                />
                <span className="ml-2">{formatPosition(position)}</span>
              </label>
            ))}
          </div>
        </div>

        {/* Submit button */}
        <Button onClick={handleSubmit} className="bg-blue-600 hover:bg-blue-700">
          Update Profile
        </Button>
      </div>
    </>
  );
}
