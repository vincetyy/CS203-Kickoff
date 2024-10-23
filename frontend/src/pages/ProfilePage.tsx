import React, { useState, useEffect } from 'react';
import { Input } from '../components/ui/input';
import { Button } from '../components/ui/button';
import { PlayerPosition, PlayerProfile } from '../types/profile';
import eyePassword from '@/assets/eyePassword.svg';
import eyePasswordOff from '@/assets/eyePasswordOff.svg';
import { toast } from 'react-hot-toast';
import { fetchPlayerProfileById, login, updatePlayerProfile } from '../services/userService';
import { getClubByPlayerId } from '../services/clubService';
import { useDispatch, useSelector } from 'react-redux';
import { selectUserId, setUser, fetchUserClubAsync } from '../store/userSlice';
import { Club } from '../types/club';
import { AppDispatch } from '../store';
import { useNavigate } from 'react-router-dom';
import ViewProfile from './ViewProfile';

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
      const response = await login(username, password);

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
              <a onClick={() => navigate("/profile/signup")} className="text-indigo-400">
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

  return (<ViewProfile />);
}
