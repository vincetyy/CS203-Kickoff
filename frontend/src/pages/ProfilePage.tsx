import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Input } from '../components/ui/input';
import { Button } from '../components/ui/button';
import { PlayerPosition, PlayerProfile } from '../types/profile';
import eyePassword from '@/assets/eyePassword.svg';
import eyePasswordOff from '@/assets/eyePasswordOff.svg';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';
import { toast } from 'react-hot-toast';
import { fetchPlayerProfileById, updatePlayerProfile } from '../services/profileService';
import { getClubByPlayerId } from '../services/clubService';
import { useDispatch, useSelector } from 'react-redux';
import { selectUserId, setUser, fetchUserClubAsync } from '../store/userSlice';
import { Club } from '../types/club';
import { AppDispatch } from '../store';
import { useNavigate } from 'react-router-dom';

export default function PlayerProfilePage() {
  const userId = useSelector(selectUserId);
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  const [playerProfile, setPlayerProfile] = useState<PlayerProfile | null>(null);
  const [club, setClub] = useState<Club | null>(null);
  const [preferredPositions, setPreferredPositions] = useState<PlayerPosition[]>([]);
  const [profileDescription, setProfileDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // States for login form
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);

  // Toggle password visibility
  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  // Handle login form submission
  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      const response = await axios.post('http://localhost:8081/users/login', {
        username,
        password,
      });

      if (response.status === 200) {
        const token = response.data.jwtToken;
        localStorage.setItem('authToken', token);
        localStorage.setItem('username', username);

        dispatch(setUser({ userId: response.data.userId, username }));
        dispatch(fetchUserClubAsync());
        toast.success(`Welcome back, ${username}`, {
          duration: 3000,
          position: 'top-center',
        });

        navigate('/clubs');
      }
    } catch (error) {
      console.error('Error during login:', error);
      toast.error('Invalid username or password', {
        duration: 4000,
        position: 'top-center',
      });
    }
  };

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

  // Conditional rendering for login or profile page
  if (!userId) {
    return (
      <div className="flex justify-center items-center ">
        <div className="max-w-sm w-full space-y-8 p-8 bg-gray-900 rounded-lg">
          <div className="text-center">
            <h2 className="mt-6 text-3xl text-white">
              Log in to see your profile.
            </h2>
          </div>

          {/* Login Form */}
          <form className="mt-8 space-y-6" onSubmit={handleLogin}>
            <div className="rounded-md shadow-sm space-y-4">
              <div>
                <label htmlFor="username" className="block text-sm font-medium text-white mb-1">
                  Username
                </label>
                <Input
                  id="username"
                  name="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                  className="w-full"
                  placeholder="Enter Username"
                />
              </div>
              <div>
                <label htmlFor="password" className="block text-sm font-medium text-white mb-1">
                  Password
                </label>
                <div className="relative">
                  <Input
                    id="password"
                    name="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    type={showPassword ? 'text' : 'password'}
                    required
                    className="w-full"
                    placeholder="Enter Password"
                  />
                  <div className="absolute inset-y-0 right-0 flex items-center px-4 text-gray-600 cursor-pointer" onClick={togglePasswordVisibility}>
                    <img src={showPassword ? eyePassword : eyePasswordOff} alt="Toggle Password Visibility" className="h-5 w-5" />
                  </div>
                </div>
              </div>
            </div>

            <Button type="submit" className="w-full bg-blue-600 hover:bg-blue-700">
              Sign in
            </Button>

            <div className="text-center text-sm text-white mt-2">
              Or sign in with Google
            </div>

            <div className="text-center text-sm text-white">
              Don’t have an account?{' '}
              <a href="/profile/signup" className="text-indigo-400">
                Sign up now
              </a>
            </div>
          </form>
        </div>
      </div>
    );
  }

  // Render profile page if user is logged in
  if (loading) return <div>Loading...</div>;

  if (error || !playerProfile) return <div>Error: {error || 'Profile not found'}</div>;

  return (
    <div className="container mx-auto p-6">
      <div className="bg-gray-900 rounded-lg p-6">
        <div className="flex items-center mb-6">
          <img
            src={`https://picsum.photos/seed/${playerProfile.id}/200/200`}
            alt={`${playerProfile.user.username}'s profile`}
            className="w-24 h-24 rounded-full object-cover mr-6"
          />
          <div>
            <h1 className="text-3xl font-bold">{playerProfile.user.username}</h1>
            <p className="text-gray-400">Player ID: {playerProfile.id}</p>
          </div>
        </div>

        <div className="mb-6">
          <h2 className="text-xl font-semibold mb-2">Club Information</h2>
          {club ? (
            <div className="flex items-center">
              <img
                src={`https://picsum.photos/seed/${club.id}/400/300`}
                alt={`${club.name} logo`}
                className="w-16 h-16 rounded-full object-cover mr-4"
              />
              <div>
                <p className="font-semibold">{club.name}</p>
                <p className="text-sm text-gray-400">ELO: {club.elo.toFixed(2)}</p>
              </div>
            </div>
          ) : (
            <p>You are not currently associated with a club.</p>
          )}
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
