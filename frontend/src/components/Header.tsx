import { useEffect, useState } from 'react';
import { Menu, Bell, MessageSquare } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Button } from './ui/button';
import { Avatar, AvatarFallback } from './ui/avatar';
import { Toaster } from 'react-hot-toast';
import { useSelector } from 'react-redux';
import { selectUsername } from '../store/userSlice';
import { useNavigate } from 'react-router-dom';
import { Club } from '../types/club'; 
import { selectUserClub } from '../store/userSlice';
import axios from 'axios';

export default function Header() {
  const [newApplications, setNewApplications] = useState(false);  // State to track if there are new applications
  const username = useSelector(selectUsername);
  const navigate = useNavigate();
  const userClub: Club | null = useSelector(selectUserClub);
  const clubId = userClub?.id;

  // Polling to check for new applications every 5 seconds
  useEffect(() => {
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
          setNewApplications(false);  // No pending applications
        }
      } catch (error) {
        console.error('Error fetching applications:', error);
        setNewApplications(false);
      }
    };

    const intervalId = setInterval(checkForNewApplications, 2000);  // Poll every 5 seconds

    // Cleanup interval on component unmount
    return () => clearInterval(intervalId);
  }, [clubId]);

  const handleBellClick = () => {
    if (clubId) {
      navigate(`/clubs/${clubId}/applications`);
    } else {
      console.error('No club selected');
    }
  };

  const avatarFallbackText = username ? username.slice(0, 2).toUpperCase() : '';

  return (
    <header className="flex justify-between items-center p-4 bg-gray-900">
      <Toaster />
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
          <Bell className="h-6 w-6 text-blue-500" /> {/* Make the bell icon blue */}
          {newApplications && (
            <span className="absolute top-0 right-0 block h-3 w-3 rounded-full bg-red-500 ring-2 ring-white" />
          )}
        </Button>
        <Button variant="ghost" size="icon">
          <MessageSquare className="h-5 w-5" />
        </Button>
        <Avatar>
          <AvatarFallback>{avatarFallbackText}</AvatarFallback>
        </Avatar>
      </div>
    </header>
  );
}
