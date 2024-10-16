import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-hot-toast';
import { Button } from '../components/ui/button';
import { ClubProfile } from '../types/club';
import { PlayerProfile } from '../types/profile';
import { selectUserId } from '../store/userSlice';
import { useSelector } from 'react-redux';

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '../components/ui/dialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../components/ui/select';
import { fetchPlayerProfileById } from '../services/profileService';

enum PlayerPosition {
  POSITION_FORWARD = 'POSITION_FORWARD',
  POSITION_MIDFIELDER = 'POSITION_MIDFIELDER',
  POSITION_DEFENDER = 'POSITION_DEFENDER',
  POSITION_GOALKEEPER = 'POSITION_GOALKEEPER',
}

const ClubInfo: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [club, setClub] = useState<ClubProfile | null>(null);
  const [captain, setCaptain] = useState<PlayerProfile | null>(null);
  const [players, setPlayers] = useState<PlayerProfile[] | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [hasApplied, setHasApplied] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
  const [selectedPosition, setSelectedPosition] = useState<PlayerPosition | null>(null);
  const userId = useSelector(selectUserId);

  useEffect(() => {
    const fetchClub = async () => {
      try {
        const clubResponse = await axios.get(`http://localhost:8082/clubs/${id}`);
        setClub(clubResponse.data);

        const applicantsResponse = await axios.get(`http://localhost:8082/clubs/${id}/applications`);
        console.log(applicantsResponse.data);
        
        setHasApplied(applicantsResponse.data.includes(userId));
        
        
        const captainResponse = await fetchPlayerProfileById(clubResponse.data.captainId);
        
        setCaptain(captainResponse);

        const playerIds = clubResponse.data.players; // Assuming clubResponse.data.players is an array of player IDs
        const playerProfiles = await Promise.all(playerIds.map((playerId: number) => fetchPlayerProfileById(playerId)));
        console.log(playerProfiles);
        
        // Store the player profiles in state
        setPlayers(playerProfiles);
      } catch (err: any) {
        console.error('Error fetching club info:', err);
        setError('Failed to fetch club information.');
      } finally {
        setLoading(false);
      }
    };

    fetchClub();
  }, [id]);

  const handleApply = async () => {
    if (!selectedPosition) {
      toast.error('Please select a position.');
      return;
    }

    if (!userId) {
      toast.error('You need to log in to apply.');
      return;
    }

    try {
      await axios.post(`http://localhost:8082/clubs/${id}/apply`, {
        playerId: userId,
        desiredPosition: selectedPosition,
      });
      toast.success('Application sent successfully!');
      setHasApplied(true);
      setIsDialogOpen(false);
      setSelectedPosition(null);
    } catch (err: any) {
      console.error('Error applying to club:', err);
      toast.error('Failed to apply to club.');
    }
  };

  const handlePositionChange = (position: string) => {
    setSelectedPosition(position as PlayerPosition);
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  if (error || !club) {
    return (
      <div className="flex justify-center items-center h-screen">
        {error || 'Club not found.'}
      </div>
    );
  }

  return (
    <div className="container mx-auto p-6">
      <img
        src={`https://picsum.photos/seed/club-${club.id}/800/200`}
        alt={`${club.name} banner`}
        className="w-full h-48 object-cover mb-4 rounded"
      />
      <h1 className="text-3xl font-bold mb-4">{club.name}</h1>
      <p className="text-lg mb-4">{club.description || 'No description available.'}</p>
      <div className="flex items-center mb-4">
        <div className="mr-4">
          <strong>Captain:</strong> {captain.user.username || 'No captain assigned.'}
        </div>
        <div>
          <strong>ELO:</strong> {club.elo ? club.elo.toFixed(2) : 'N/A'}
        </div>
      </div>

      {/* Players List */}
      <div className="mb-4">
        <h2 className="text-2xl font-semibold mb-2">Players in the Club</h2>
        <ul className="list-disc list-inside mt-2">
        {players ? (
          players.map((player, index) => (
            <li key={index}>{player.user.username}</li>
          ))
        ) : (
          <p>Loading player profiles...</p>
        )}
        </ul>
      </div>

      {/* Future Tournaments Section */}
      <div className="mb-4">
        <h2 className="text-2xl font-semibold mb-2">Past Tournaments</h2>
        {/* Implement fetching and displaying past tournaments in the future */}
        <p>No tournament data available yet.</p>
      </div>

      {/* Apply Button */}
      {
        userId && !hasApplied &&
        <Button onClick={() => setIsDialogOpen(true)}>Apply to Join</Button>
      }

      {
        userId && hasApplied &&
        <Button className="bg-green-500 hover:bg-green-600">Applied!</Button>
      }   

      {/* Position Selection Dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Apply to {club.name}</DialogTitle>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="flex flex-col justify-between">
              <Select onValueChange={handlePositionChange}>
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Select your preferred position" />
                </SelectTrigger>
                <SelectContent>
                  {Object.values(PlayerPosition).map((position) => (
                    <SelectItem key={position} value={position}>
                      {position.replace('POSITION_', '').charAt(0) +
                        position.replace('POSITION_', '').slice(1).toLowerCase()}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
          <div className="flex flex-col sm:flex-row justify-between mt-4 space-y-2 sm:space-y-0 sm:space-x-2">
            <Button
              variant="secondary"
              onClick={() => setIsDialogOpen(false)}
              className="w-full"
            >
              Close
            </Button>
            <Button
              onClick={handleApply}
              className="w-full"
              disabled={!selectedPosition}
            >
              Apply
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default ClubInfo;