import { useEffect, useState } from 'react';
import { Menu, Bell, MessageSquare } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Button } from './ui/button';
import { Avatar, AvatarFallback } from './ui/avatar';
import { Toaster, toast } from 'react-hot-toast';
import { useSelector, useDispatch } from 'react-redux'; // Correct hook usage inside functional component
import { selectUsername, selectUserId } from '../store/userSlice';
import { useNavigate } from 'react-router-dom';
import { Club } from '../types/club';
import { selectUserClub, clearUser } from '../store/userSlice';
import axios from 'axios';

export default function Header() {
  const [newApplications, setNewApplications] = useState(false);
  const username = useSelector(selectUsername); // Use useSelector hook inside the component body
  const userClub: Club | null = useSelector(selectUserClub); // Same here
  const userId = useSelector(selectUserId);
  const navigate = useNavigate();
  const dispatch = useDispatch(); // Use useDispatch inside the component body
  const clubId = userClub?.id;

  useEffect(() => {
    if (userId) {
      console.log("Updated userId after dispatch: ", userId); // Log the userId
    }
    const checkForNewApplications = async () => {
      if (!clubId) return;

      try {
        const baseUrl = 'http://localhost:8082';
        const response = await axios.get(`${baseUrl}/clubs/${clubId}/applications`);

        if (response.status === 200) {
          const playerIds = response.data;
          const hasPending = playerIds.length > 0;  // If there are any pending applications, set the flag
          setNewApplications(hasPending);
        } else {
          setNewApplications(false);
        }
      } catch (error) {
        console.error('Error fetching applications:', error);
        setNewApplications(false);
      }
    };

    const intervalId = setInterval(checkForNewApplications, 2000);  // Poll every 5 seconds

    return () => clearInterval(intervalId);
  }, [clubId]);

  const handleBellClick = () => {
    if (clubId) {
      navigate(`/clubs/${clubId}/applications`);
    } else {
      console.error('No club selected');
    }
  };

  const handleLogoutClick = () => {
    // Clear the auth token from localStorage
    localStorage.removeItem('authToken');

    // Dispatch a logout action to clear persisted user data
    dispatch(clearUser()); // Use dispatch here safely

    // Optionally, show a toast to confirm the logout action
    toast('You have been logged out.');
  };

  const avatarFallbackText = username ? username.slice(0, 2).toUpperCase() : '';

  return (
    <header className="flex justify-between items-center p-4 bg-gray-900">
      <Toaster /> {/* This is needed for toast notifications */}
      <div className="flex items-center">
        <Link to="/" className="text-2xl font-bold ml-2 text-white hover:text-gray-300 transition-colors">
          KICKOFF
        </Link>
      </div>
      <div className="flex items-center space-x-4">
        <Button
          variant="ghost"
          className="relative"
          onClick={handleBellClick}
        >
          <Bell className="h-6 w-6 text-blue-500" />
          {newApplications && (
            <span className="absolute top-0 right-0 block h-3 w-3 rounded-full bg-red-500 ring-2 ring-white" />
          )}
        </Button>
        <Button variant="ghost" size="icon">
          <MessageSquare className="h-5 w-5" />
        </Button>
        <Button variant="ghost" onClick={handleLogoutClick}> {/* Logout Button */}
          Logout
        </Button>
        <Avatar>
          <AvatarFallback>{avatarFallbackText}</AvatarFallback>
        </Avatar>
      </div>
    </header>
  );
}
