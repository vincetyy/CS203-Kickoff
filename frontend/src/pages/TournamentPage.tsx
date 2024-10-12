// src/pages/TournamentPage.tsx

import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Toaster, toast } from 'react-hot-toast';
import { AppDispatch } from '../store';
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";

import { fetchTournamentById, } from '../services/tournamentService';
import { Tournament, Club } from '../types/tournament';
import { useDispatch, useSelector } from 'react-redux';
import { removeClubFromTournamentAsync, updateTournamentAsync } from '../store/tournamentSlice';
import { selectUserId } from '../store/userSlice';

import UpdateTournament from '../components/UpdateTournament';
import { TournamentUpdate } from '../types/tournament';

const TournamentPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();

  const [isRemoveDialogOpen, setIsRemoveDialogOpen] = useState(false);
  const [clubToRemove, setClubToRemove] = useState<Club | null>(null);

  // State for Update Tournament Dialog
  const [isUpdateDialogOpen, setIsUpdateDialogOpen] = useState(false);
  const [initialUpdateData, setInitialUpdateData] = useState<TournamentUpdate>({
    name: '',
    startDateTime: '',
    endDateTime: '',
    location: null,
    prizePool: [],
    minRank: 0,
    maxRank: 0,
    joinedClubs: [],
  });

  // Open the dialog and set the club to be deleted
  const handleOpenRemoveDialog = (club: Club) => {
    setClubToRemove(club);  // Set the club to delete
    setIsRemoveDialogOpen(true);   // Open the dialog
  };

  // Handle confirming the deletion
  const handleConfirmRemove = () => {
    if (clubToRemove) {
      handleRemoveClub(clubToRemove.id); // Call the actual delete function with the club's id
    }
    setIsRemoveDialogOpen(false); // Close the dialog
  };

  const handleRemoveClub = async (clubId: number) => {
    try {
      // Handle case where tournamentId is invalid
      if (selectedTournament === null) {
        toast.error('Invalid tournament');
        return;
      }

      await dispatch(removeClubFromTournamentAsync({ tournamentId: selectedTournament.id, clubId })).unwrap();
  
      // Fetch the updated tournament data after removal
      const updatedTournamentData = await fetchTournamentById(selectedTournament.id);
  
      // Update the tournament details
      setSelectedTournament(updatedTournamentData);
  
      // Show a success toast notification
      toast.success('Club removed successfully!', {
        duration: 3000,
        position: 'top-center',
      });
    } catch (error: any) {
      // Handle error case
      console.error('Failed to remove the club:', error);
  
      // Show error toast notification
      toast.error('Failed to remove the club. Please try again.');
    }
  };

  const { id } = useParams<{ id: string }>();
  const tournamentId = id ? parseInt(id, 10) : null;
  const userId = useSelector(selectUserId);

  const tournamentFormatMap: { [key: string]: string } = {
    FIVE_SIDE: 'Five-a-side',
    SEVEN_SIDE: 'Seven-a-side'
  };

  const knockoutFormatMap: { [key: string]: string } = {
    SINGLE_ELIM: 'Single Elimination',
    DOUBLE_ELIM: 'Double Elimination'
  };

  const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null);
  const [status, setStatus] = useState<'idle' | 'loading' | 'succeeded' | 'failed'>('idle');
  const [error, setError] = useState<string | null>(null); 
  let isHost = false;
  if (selectedTournament) {
    console.log(selectedTournament);
    console.log(userId);
    
    isHost = selectedTournament.host === userId;
  }

  const handleBackClick = () => {
    navigate('/tournaments'); // Navigate back to /tournaments
  };

  useEffect(() => {
    if (tournamentId === null || isNaN(tournamentId)) {
      setError('Invalid tournament ID.');
      setStatus('failed');
      return;
    }

    const fetchData = async () => {
      setStatus('loading');
      setError(null);
      try {
        const tournament = await fetchTournamentById(tournamentId);
        setSelectedTournament(tournament);
        setStatus('succeeded');
      } catch (err: any) {
        console.error('Failed to fetch tournament:', err);
        setError(err.message || 'Failed to fetch tournament.');
        setStatus('failed');
      }
    };

    fetchData();
  }, [tournamentId]);

  const formatDate = (dateString: string) => {
    const options: Intl.DateTimeFormatOptions = { 
      year: 'numeric', month: 'long', day: 'numeric', 
      hour: '2-digit', minute: '2-digit' 
    };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  const handleUpdateClick = () => {
    if (selectedTournament) {
      const initialData: TournamentUpdate = {
        name: selectedTournament.name,
        startDateTime: selectedTournament.startDateTime,
        endDateTime: selectedTournament.endDateTime,
        location: selectedTournament.location,
        prizePool: selectedTournament.prizePool,
        minRank: selectedTournament.minRank,
        maxRank: selectedTournament.maxRank,
      };
      setInitialUpdateData(initialData);
      setIsUpdateDialogOpen(true);
    }
  };

  const handleUpdateTournament = async (data: TournamentUpdate) => {
    if (selectedTournament === null || tournamentId === null) {
      throw new Error('Invalid tournament data.');
    }

    // Dispatch the update action
    await dispatch(updateTournamentAsync({ 
      tournamentId: selectedTournament.id,
      tournamentData: data
    })).unwrap();

    // Fetch the updated tournament data
    const updatedTournamentData = await fetchTournamentById(tournamentId);
    setSelectedTournament(updatedTournamentData);
  };

  if (status === 'loading') return <div className="text-center mt-10">Loading tournament details...</div>;
  if (status === 'failed') return <div className="text-center mt-10 text-red-500">Error: {error}</div>;
  if (!selectedTournament) return <div className="text-center mt-10">No tournament found.</div>;

  return (
    <>
      <Toaster />
      {/* Tournament Details Banner */}
      <div className="bg-green-600 rounded-lg p-4 lg:p-6 mb-6 flex items-center space-x-4">
        <div className="bg-white rounded-full p-2 lg:p-3">
          <svg className="h-6 w-6 lg:h-8 lg:w-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
        </div>
        <div>
          <h2 className="text-xl lg:text-2xl font-bold">{selectedTournament.name}</h2>
          <p className="text-sm lg:text-base">{selectedTournament.location.name}</p>
        </div>
      </div>

      {/* Tournament Information */}
      <div className="bg-gray-800 rounded-lg p-6 mb-6">
        <h3 className="text-2xl font-semibold mb-4">Tournament Details</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p><strong>Start Date & Time:</strong> {formatDate(selectedTournament.startDateTime)}</p>
            <p><strong>End Date & Time:</strong> {formatDate(selectedTournament.endDateTime)}</p>
            <p><strong>Location:</strong> {selectedTournament.location.name}</p>
          </div>
          <div>
            <p><strong>Max Teams:</strong> {selectedTournament.maxTeams}</p>
            <p><strong>Tournament Format:</strong> {tournamentFormatMap[selectedTournament.tournamentFormat]}</p>
            <p><strong>Knockout Format:</strong> {knockoutFormatMap[selectedTournament.knockoutFormat]}</p>
            <p><strong>Prize Pool:</strong> {selectedTournament.prizePool && selectedTournament.prizePool.length > 0 ? `$${selectedTournament.prizePool.join(', ')}` : 'N/A'}</p>
            <p><strong>Rank Range:</strong> {selectedTournament.minRank !== null && selectedTournament.maxRank !== null ? `${selectedTournament.minRank} - ${selectedTournament.maxRank}` : 'N/A'}</p>
          </div>
        </div>
      </div>

      {/* Joined Clubs */}
      <div className="bg-gray-800 rounded-lg p-6 mb-6">
        <h3 className="text-2xl font-semibold mb-4">Joined Clubs</h3>
        {selectedTournament.joinedClubs.length === 0 ? (
          <p>No clubs have joined this tournament yet.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {selectedTournament.joinedClubs.map((club: Club) => (
              <div key={club.id} className="bg-gray-700 rounded-lg p-4 flex items-center justify-between space-x-4">
                <div className="flex items-center space-x-4">
                  <img 
                    src={`https://picsum.photos/seed/${club.id}/100/100`} 
                    alt={club.name} 
                    className="w-16 h-16 rounded-full object-cover" 
                  />
                  <div>
                    <h4 className="text-lg font-bold">{club.name}</h4>
                  </div>
                </div>
                {/* Conditionally render Delete button based on isHost */}
                {isHost && (
                  <button 
                    onClick={() => handleOpenRemoveDialog(club)} 
                    className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
                  >
                    Remove
                  </button>
                )}
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Remove Confirmation Dialog */}
      <Dialog open={isRemoveDialogOpen} onOpenChange={setIsRemoveDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Remove {clubToRemove?.name}</DialogTitle>
          </DialogHeader>
          <div className="mt-4">
            <p>Are you sure you want to remove {clubToRemove?.name} from this tournament?</p>
          </div>
          <div className="flex flex-col sm:flex-row justify-between mt-4 space-y-2 sm:space-y-0 sm:space-x-2">
            <button 
              className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 w-full" 
              onClick={() => setIsRemoveDialogOpen(false)}
            >
              Cancel
            </button>
            <button 
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 w-full"
              onClick={handleConfirmRemove}
            >
              Confirm
            </button>
          </div>
        </DialogContent>
      </Dialog>

      {/* Update Tournament Dialog */}
      <UpdateTournament
        isOpen={isUpdateDialogOpen}
        onClose={() => setIsUpdateDialogOpen(false)}
        initialData={initialUpdateData}
        onUpdate={handleUpdateTournament}
      />

      {/* Back and Update Buttons */}
      <div className="flex space-x-3 mb-4">
        {isHost &&
          <Button 
            type="button" 
            onClick={handleUpdateClick} 
            className="bg-blue-600 hover:bg-blue-700"
          >
            Update Tournament
          </Button>
        }
        <Button onClick={handleBackClick}>Back to Tournaments</Button>
      </div>
    </>
  );
};

export default TournamentPage;
