import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { Dispatch } from 'redux';
import { Button } from '../components/ui/button';
import { toast } from 'react-hot-toast';
import { fetchUserClubAsync, selectUserId, selectUserClub } from '../store/userSlice';
import PlayerProfileCard from '../components/PlayerProfileCard';
import { Club } from '../types/club';
import axios from 'axios';

interface Application {
  playerId: number;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
}

export default function ApplicationsPage() {
  const [applications, setApplications] = useState<Application[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const dispatch: Dispatch = useDispatch();

  const userId = useSelector(selectUserId);
  const userClub: Club | null = useSelector(selectUserClub);
  const clubId = userClub?.id;

  // Fetch user club on component mount
  useEffect(() => {
    const fetchData = async () => {
      if (!userId) {
        toast.error('User not logged in');
        navigate('/login');
        return;
      }

      try {
        await dispatch(fetchUserClubAsync() as any);
        setIsLoading(false);
      } catch (error) {
        console.error('Error fetching user club:', error);
        toast.error('Failed to fetch user club');
        setIsLoading(false);
      }
    };

    fetchData();
  }, [userId, dispatch, navigate]);

  // Fetch applications when clubId is available
  useEffect(() => {
    const fetchApplications = async () => {
      if (!clubId) return;
      try {
        const baseUrl = 'http://localhost:8082';
        const response = await axios.get(`${baseUrl}/clubs/${clubId}/applications`);

        if (response.status === 200) {
          const playerIds: number[] = response.data;
          const newApplications = playerIds.map(playerId => ({
            playerId,
            status: 'PENDING' as const
          }));

          setApplications(newApplications);
        } else {
          throw new Error('Failed to fetch applications');
        }
      } catch (error) {
        console.error('Error fetching applications:', error);
        toast.error('Failed to fetch applications');
      }
    };

    if (clubId) {
      fetchApplications();
    }
  }, [clubId]);

  // Handle accept/reject application status
  const handleApplicationUpdate = async (playerId: number, status: 'ACCEPTED' | 'REJECTED') => {
    if (!clubId) return;

    try {
      const updateResponse = await axios.post(
        `http://localhost:8082/clubs/${clubId}/applications/${playerId}`,
        { applicationStatus: status },  
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`,  
        },
      });

      if (updateResponse.status === 200) {
        toast.success(`Application ${status.toLowerCase()} successfully!`);

        // Update the applications state to reflect the new status
        setApplications(prevApplications =>
          prevApplications.map(app =>
            app.playerId === playerId ? { ...app, status } : app
          )
        );
      } else {
        throw new Error('Failed to update application');
      }
    } catch (error) {
      console.error(`Error ${status.toLowerCase()}ing application:`, error);
      toast.error(`Failed to ${status.toLowerCase()} application`);
    }
  };

  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (!userId) {
    return <p>Please log in to view applications.</p>;
  }

  if (!clubId) {
    return <p>You are not a member of any club.</p>;
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-6">Player Applications</h1>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {applications.map((application) => (
          <div key={application.playerId}>
            <PlayerProfileCard 
              id={application.playerId} 
              availability={application.status === 'PENDING'}
            />
            <p className="mt-2">Status: {application.status}</p>
            {application.status === 'PENDING' && (
              <div className="mt-4 space-x-2">
                <Button onClick={() => handleApplicationUpdate(application.playerId, 'ACCEPTED')}>Accept</Button>
                <Button onClick={() => handleApplicationUpdate(application.playerId, 'REJECTED')} className="bg-red-500 hover:bg-red-600 text-white">Reject</Button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
