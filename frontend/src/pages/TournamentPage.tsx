// src/pages/TournamentPage.tsx

import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { Button } from "../components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";

import { fetchTournamentById } from '../services/tournamentService';
import { Tournament, Club } from '../types/tournament';

const TournamentPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const tournamentId = id ? parseInt(id, 10) : null;

  // Retrieve clubId from the user slice in Redux store
  // const clubId = useSelector((state: RootState) => state.user.clubId); // Adjust based on your user slice

  const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null);
  const [status, setStatus] = useState<'idle' | 'loading' | 'succeeded' | 'failed'>('idle');
  const [error, setError] = useState<string | null>(null);
  // const [joinRole, setJoinRole] = useState<TournamentJoinRole | null>(null);

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
    </>
  );
};

export default TournamentPage;
