import { useState, useEffect } from 'react';
import { Input } from '../components/ui/input';
import { Button } from '../components/ui/button';
import { PlayerPosition, PlayerProfile } from '../types/profile';
import { fetchPlayerProfileById, updatePlayerProfile } from '../services/userService';
import { getClubByPlayerId } from '../services/clubService';
import { useSelector } from 'react-redux';
import { selectUserId } from '../store/userSlice';
import { Club } from '../types/club';
import { toast } from 'react-hot-toast';
import { ArrowLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function PlayerProfilePage() {
  const userId = useSelector(selectUserId);
  const navigate = useNavigate();

  const [playerProfile, setPlayerProfile] = useState<PlayerProfile | null>(null);
  const [club, setClub] = useState<Club | null>(null);
  const [preferredPositions, setPreferredPositions] = useState<PlayerPosition[]>([]);
  const [profileDescription, setProfileDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);


  // Fetch player profile when logged in
  useEffect(() => {
    if (!userId) {
      setError('User not logged in');
      setLoading(false);
      return;
    }

    const fetchPlayerProfile = async () => {
      try {
        const response = await fetchPlayerProfileById(userId);
        setPlayerProfile(response);
        setPreferredPositions(response.preferredPositions || []);
        setProfileDescription(response.profileDescription || '');
        setLoading(false);
      } catch (err) {
        console.error('Error fetching player profile:', err);
        setLoading(false);
      }

      try {
        const clubResponse = await getClubByPlayerId(userId);
        setClub(clubResponse);
      } catch (err) {
        console.error('Error fetching club:', err);
      }
    };

    fetchPlayerProfile();
  }, [userId]);

  const handlePreferredPositionsChange = (position: PlayerPosition) => {
    setPreferredPositions((prevPositions) =>
      prevPositions.includes(position)
        ? prevPositions.filter((pos) => pos !== position)
        : [...prevPositions, position]
    );
  };

  const handleSubmit = async () => {
    if (!playerProfile) return;

    try {
      await updatePlayerProfile(playerProfile.id, preferredPositions, profileDescription);
      toast.success('Profile updated successfully', {
        duration: 3000,
        position: 'top-center',
      });
      navigate("/profile");
    } catch (err) {
      console.error('Error updating profile:', err);
      toast.error('Failed to update profile', {
        duration: 4000,
        position: 'top-center',
      });
    }
  };

  const formatPosition = (position: string) => {
    return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
  };

  // Render profile page if user is logged in
  if (loading) return <div>Loading...</div>;

  if (error || !playerProfile) return <div>Error: {error || 'Profile not found'}</div>;

  return (
    <div className="container mx-auto p-6">
    <div className='pb-2'>
        <Button variant="ghost" size="icon" onClick={() => navigate(-1)}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
      </div>
      <div className="bg-gray-900 rounded-lg p-6">
        <div className="flex items-center mb-6">
          <img
            src={`https://picsum.photos/seed/${playerProfile.id + 2000}/200/200`}
            alt={`${playerProfile.username}'s profile`}
            className="w-24 h-24 rounded-full object-cover mr-6"
          />
          <div>
            <h1 className="text-3xl font-bold">{playerProfile. username}</h1>
            <p className="text-gray-400">Player ID: {playerProfile.id}</p>
          </div>
        </div>

        <div className="mb-6">
          <h2 className="text-xl font-semibold mb-2">Profile Description</h2>
          <Input
            value={profileDescription}
            onChange={(e) => setProfileDescription(e.target.value)}
            placeholder="Describe yourself"
            className="w-full bg-gray-800 border-gray-700"
          />
        </div>

        <div className="mb-6">
          <h2 className="text-xl font-semibold mb-2">Preferred Positions</h2>
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

        <Button onClick={handleSubmit} className="bg-blue-600 hover:bg-blue-700">
          Update Profile
        </Button>
      </div>
    </div>
  );
}
