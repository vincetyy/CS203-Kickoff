// src/pages/TournamentPage.tsx

import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import { Toaster, toast } from 'react-hot-toast';
import { AppDispatch, RootState } from '../store'
import { Input } from "../components/ui/input"
import { Button } from "../components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";

import { fetchTournamentById, updateTournament } from '../services/tournamentService';
import { Tournament, Club } from '../types/tournament';
import { useDispatch } from 'react-redux';
import { updateTournamentAsync } from '../store/tournamentSlice';


const TournamentPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>()

  const { id } = useParams<{ id: string }>();
  const tournamentId = id ? parseInt(id, 10) : null;

  const tournamentFormatMap: { [key: string]: string } = {
    FIVE_SIDE: 'Five-a-side',
    SEVEN_SIDE: 'Seven-a-side'
  };

  const knockoutFormatMap: { [key: string]: string } = {
    SINGLE_ELIM: 'Single Elimination',
    DOUBLE_ELIM: 'Double Elimination'
  };

  // Retrieve clubId from the user slice in Redux store
  // const clubId = useSelector((state: RootState) => state.user.clubId); // Adjust based on your user slice

  const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null);
  const [status, setStatus] = useState<'idle' | 'loading' | 'succeeded' | 'failed'>('idle');
  const [error, setError] = useState<string | null>(null); 
  let isHost = false;
  if (selectedTournament) {
    // hardcoded rn
    isHost = selectedTournament.host.id === 1;
    console.log(isHost);
    
  }
  // const [joinRole, setJoinRole] = useState<TournamentJoinRole | null>(null);

  const [updatedTournament, setUpdatedTournament] = useState({
    name: '',
    startDateTime: '',
    endDateTime: '',
    locationId: '',
    maxTeams: 0,
    tournamentFormat: '',
    knockoutFormat: '',
    minRank: 0,
    maxRank: 0,
  })
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)

  const handleUpdate = () => {
    if (selectedTournament) {
      setUpdatedTournament({
        name: selectedTournament.name || '',
        locationId: selectedTournament.location.id || '',
        startDateTime: selectedTournament.startDateTime || '',
        endDateTime: selectedTournament.endDateTime || '',
        maxTeams: selectedTournament.maxTeams || 0,
        tournamentFormat: selectedTournament.tournamentFormat || '',
        knockoutFormat: selectedTournament.knockoutFormat || '',
        minRank: selectedTournament.minRank || 0,
        maxRank: selectedTournament.maxRank || 0,
      });
      setIsCreateDialogOpen(true);
    }
  };

  const handleUpdateTournament = async () => {
    console.log(updatedTournament)
    if (!updatedTournament.name || !updatedTournament.startDateTime || !updatedTournament.endDateTime || !updatedTournament.locationId || !updatedTournament.maxTeams || !updatedTournament.tournamentFormat || !updatedTournament.knockoutFormat) {
      toast.error('Please fill in all required fields', {
        duration: 3000,
        position: 'top-center',
      })
      return
    }

    setIsCreateDialogOpen(false) // Close the dialog immediately

    try {

      const result = await dispatch(updateTournamentAsync({ 
        tournamentId: selectedTournament.id,
        tournamentData: updatedTournament
      })).unwrap();

      if (tournamentId === null || isNaN(tournamentId)) {
        setError('Invalid tournament ID.');
        setStatus('failed');
        return;
      }
      const updatedTournamentData = await fetchTournamentById(tournamentId);
      setSelectedTournament(updatedTournamentData); // Update the state with the new details
      
      setIsCreateDialogOpen(false)
      
      // Show the success toast
      toast.success('Tournament updated successfully!', {
        duration: 3000,
        position: 'top-center',
      });

      // Reset the form
      setUpdatedTournament({
        name: '',
        startDateTime: '',
        endDateTime: '',
        locationId: '',
        maxTeams: 0,
        tournamentFormat: '',
        knockoutFormat: '',
        minRank: 0,
        maxRank: 0,
      });

    } catch (err) {
      console.error('Error updating tournament:', err)
      toast.error(`Failed to update tournament: ${err.message}`, {
        duration: 4000,
        position: 'top-center',
      })
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setUpdatedTournament(prev => ({ ...prev, [name]: value }))
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

  if (status === 'loading') return <div className="text-center mt-10">Loading tournament details...</div>;
  if (status === 'failed') return <div className="text-center mt-10 text-red-500">Error: {error}</div>;
  if (!selectedTournament) return <div className="text-center mt-10">No tournament found.</div>;

  return (
    <>
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
            <p><strong>Tournament Format:</strong> {selectedTournament.tournamentFormat.replace('_', ' ')}</p>
            <p><strong>Knockout Format:</strong> {selectedTournament.knockoutFormat.replace('_', ' ')}</p>
            <p><strong>Prize Pool:</strong> {selectedTournament.prizePool ? `$${selectedTournament.prizePool}` : 'N/A'}</p>
            <p><strong>Rank Range:</strong> {selectedTournament.minRank && selectedTournament.maxRank ? `${selectedTournament.minRank} - ${selectedTournament.maxRank}` : 'N/A'}</p>
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
              <div key={club.id} className="bg-gray-700 rounded-lg p-4 flex items-center space-x-4">
                <img 
                  src={`https://picsum.photos/seed/${club.id}/100/100`} 
                  alt={club.name} 
                  className="w-16 h-16 rounded-full object-cover" 
                />
                <div>
                  <h4 className="text-lg font-bold">{club.name}</h4>
                </div>
              </div>
            ))}
          </div>
        )}

        
      </div>
      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent className="sm:max-w-[600px] lg:max-w-[800px]">
          <DialogHeader>
            <DialogTitle>Create New Tournament</DialogTitle>
          </DialogHeader>
          <div className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label htmlFor="name" className="form-label">Tournament Name</label>
                <Input
                  id="name"
                  name="name"
                  value={updatedTournament.name}
                  onChange={handleInputChange}
                  className="form-input"
                  required
                />
              </div>
              <div>
                <label htmlFor="locationId" className="form-label">Location ID</label>
                <Input
                  id="locationId"
                  name="locationId"
                  type="number"
                  value={updatedTournament.locationId}
                  onChange={handleInputChange}
                  className="form-input"
                  required
                />
              </div>
              <div>
                <label htmlFor="startDateTime" className="form-label">Start Date & Time</label>
                <Input
                  id="startDateTime"
                  name="startDateTime"
                  type="datetime-local"
                  value={updatedTournament.startDateTime}
                  onChange={handleInputChange}
                  className="form-input"
                  required
                />
              </div>
              <div>
                <label htmlFor="endDateTime" className="form-label">End Date & Time</label>
                <Input
                  id="endDateTime"
                  name="endDateTime"
                  type="datetime-local"
                  value={updatedTournament.endDateTime}
                  onChange={handleInputChange}
                  className="form-input"
                  required
                />
              </div>
              <div>
                <label htmlFor="maxTeams" className="form-label">Max Teams</label>
                <Input
                  id="maxTeams"
                  name="maxTeams"
                  type="number"
                  value={updatedTournament.maxTeams}
                  onChange={handleInputChange}
                  className="form-input"
                  required
                />
              </div>
              <div>
                <label htmlFor="tournamentFormat" className="form-label">Tournament Format</label>
                <Select defaultValue={updatedTournament.tournamentFormat} 
                  defaultDisplayValue={tournamentFormatMap[updatedTournament.tournamentFormat]}
                  onValueChange={(value) => setUpdatedTournament(prev => ({ ...prev, tournamentFormat: value }))
                }>
                  <SelectTrigger className="select-trigger">
                    <SelectValue placeholder="Select format" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="FIVE_SIDE">Five-a-side</SelectItem>
                    <SelectItem value="SEVEN_SIDE">Seven-a-side</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <label htmlFor="knockoutFormat" className="form-label">Knockout Format</label>
                <Select defaultValue={updatedTournament.knockoutFormat} 
                  defaultDisplayValue={knockoutFormatMap[updatedTournament.knockoutFormat]}
                  onValueChange={(value) => setUpdatedTournament(prev => ({ ...prev, knockoutFormat: value }))
                }>
                  <SelectTrigger className="select-trigger">
                    <SelectValue placeholder="Select format" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="SINGLE_ELIM">Single Elimination</SelectItem>
                    <SelectItem value="DOUBLE_ELIM">Double Elimination</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <label htmlFor="minRank" className="form-label">Min Rank</label>
                <Input
                  id="minRank"
                  name="minRank"
                  type="number"
                  value={updatedTournament.minRank}
                  onChange={handleInputChange}
                  className="form-input"
                />
              </div>
              <div>
                <label htmlFor="maxRank" className="form-label">Max Rank</label>
                <Input
                  id="maxRank"
                  name="maxRank"
                  type="number"
                  value={updatedTournament.maxRank}
                  onChange={handleInputChange}
                  className="form-input"
                />
              </div>
            </div>
            <div className="flex justify-end space-x-2 mt-6">
              <Button type="button" onClick={() => setIsCreateDialogOpen(false)} className="bg-gray-600 hover:bg-gray-700">
                Cancel
              </Button>
              <Button type="button" onClick={handleUpdateTournament} className="bg-blue-600 hover:bg-blue-700">
                Update
              </Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>
      
      {/* Back Button */}
      <div className="flex space-x-3 mb-4">
        {isHost &&
          <Button type="button" onClick={handleUpdate} className="bg-blue-600 hover:bg-blue-700">
            Update
          </Button>
        }
        <Button onClick={handleBackClick}>Back to Tournaments</Button>
      </div>
      
    </>
  );
};

export default TournamentPage;
