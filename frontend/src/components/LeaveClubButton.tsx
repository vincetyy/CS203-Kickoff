import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button } from '../components/ui/button';
import { toast } from 'react-hot-toast';
import { fetchUserClubAsync, selectUserId, selectUserClub } from '../store/userSlice';
import { Club } from '../types/club';

const LeaveClubButton: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const userId = useSelector(selectUserId);
  const userClub: Club | null = useSelector(selectUserClub);
  const clubId = userClub?.id;

  const handleLeaveClub = async () => {
    if (!userId || !clubId) {
      toast.error('User or club information not available');
      return;
    }

    try {
        const response = await axios.patch(
            `http://localhost:8082/clubs/${clubId}/leavePlayer`,
            { playerId: userId },  // Ensure this matches the backend's expected structure
            {
              headers: {
                'Content-Type': 'application/json',  // You still need to specify content type
              },
            }
          );     
    
        if (response.status === 200) {
          toast.success('You have left the club successfully');
          // Dispatch an action to update the Redux store
          dispatch(fetchUserClubAsync() as any);
          navigate('/profile');
        } else {
          throw new Error('Failed to leave the club');
        }
      } catch (error) {
        console.error('Error leaving club:', error);
        toast.error('An error occurred while trying to leave the club');
      }
  };

  return (
    <Button 
      onClick={handleLeaveClub}
      className="bg-red-500 hover:bg-red-600 text-white"
    >
      Leave Club
    </Button>
  );
};

export default LeaveClubButton;
