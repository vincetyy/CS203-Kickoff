import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-hot-toast';
import { Button } from '../components/ui/button';
import { ClubProfile } from '../types/club';
import PlayerProfileCard from '../components/PlayerProfileCard';
import { PlayerPosition } from '../types/profile';

enum TournamentFilter {
  UPCOMING = 'UPCOMING',
  CURRENT = 'CURRENT',
  PAST = 'PAST',
}

interface Tournament {
  id: number;
  name: string;
  startDateTime: string;
  endDateTime: string;
}

interface Player {
  id: number;
  user: {
    username: string;
  };
  preferredPositions: PlayerPosition[];
}

// Adjust ClubProfile to include the captain
export interface ClubProfile {
    id: number;
    name: string;
    clubDescription: string;
    elo: number;
    captain: Player;  // Captain is a Player
    players: Player[];  // List of players
}

const ClubDashboard: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [club, setClub] = useState<ClubProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [tournamentFilter, setTournamentFilter] = useState<TournamentFilter>(TournamentFilter.UPCOMING);

  useEffect(() => {
    const fetchClub = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/clubs/${id}`);
        console.log('Club data:', response.data);
        setClub(response.data);
      } catch (err: any) {
        console.error('Error fetching club info:', err);
        setError(err.response?.data?.message || 'Failed to fetch club information.');
      } finally {
        setLoading(false);
      }
    };

    fetchClub();
  }, [id]);

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/tournaments/${id}/tournaments`, {
          params: { filter: tournamentFilter }
        });
        setTournaments(response.data);
      } catch (err: any) {
        console.error('Error fetching tournaments:', err);
        toast.error('Failed to fetch tournaments.');
      }
    };

    fetchTournaments();
  }, [id, tournamentFilter]);

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

  // Combine captain and players into a single array
  const allPlayers = [club.captain, ...(club.players || [])].filter(
    (player) => player && player.user && player.user.username
  );
  
  return (
    <div className="container mx-auto p-6">
      <img
        src={`https://picsum.photos/seed/club-${club.id}/800/200`}
        alt={`${club.name} banner`}
        className="w-full h-48 object-cover mb-4 rounded"
      />
      <h1 className="text-3xl font-bold mb-4">{club.name}</h1>
      <p className="text-lg mb-4">{club.clubDescription || 'No description available.'}</p>
      <div className="flex items-center mb-4">
        <div className="mr-4">
          <strong>Captain:</strong> {club.captain?.user?.username || 'No captain assigned.'}
        </div>
        <div>
          <strong>ELO:</strong> {club.elo ? club.elo.toFixed(2) : 'N/A'}
        </div>
      </div>

      {/* Players List */}
      <div className="mb-4">
        <h2 className="text-2xl font-semibold mb-2">Players in the Club</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {allPlayers && allPlayers.length > 0 ? (
            allPlayers.map((player: Player) => (
              <PlayerProfileCard
                key={player.id}
                id={player.id}
                name={player?.user?.username || 'Unknown Player'}
                preferredPosition={player.preferredPositions?.[0]}
              />
            ))
          ) : (
            <p>No players in this club.</p>
          )}
        </div>
      </div>

      {/* Tournament Filter Buttons */}
      <div className="flex justify-center space-x-4 mb-4">
        <Button
          onClick={() => setTournamentFilter(TournamentFilter.UPCOMING)}
          variant={tournamentFilter === TournamentFilter.UPCOMING ? "default" : "secondary"}
        >
          Upcoming Tournaments
        </Button>
        <Button
          onClick={() => setTournamentFilter(TournamentFilter.CURRENT)}
          variant={tournamentFilter === TournamentFilter.CURRENT ? "default" : "secondary"}
        >
          Current Tournaments
        </Button>
        <Button
          onClick={() => setTournamentFilter(TournamentFilter.PAST)}
          variant={tournamentFilter === TournamentFilter.PAST ? "default" : "secondary"}
        >
          Past Tournaments
        </Button>
      </div>

      {/* Tournaments Section */}
      <div className="mb-4">
        <h2 className="text-2xl font-semibold mb-2">
          {tournamentFilter === TournamentFilter.UPCOMING && "Upcoming Tournaments"}
          {tournamentFilter === TournamentFilter.CURRENT && "Current Tournaments"}
          {tournamentFilter === TournamentFilter.PAST && "Past Tournaments"}
        </h2>
        {tournaments.length > 0 ? (
          <ul>
            {tournaments.map((tournament) => (
              <li key={tournament.id}>
                <strong>{tournament.name}</strong> - Starts: {new Date(tournament.startDateTime).toLocaleString()}, Ends: {new Date(tournament.endDateTime).toLocaleString()}
              </li>
            ))}
          </ul>
        ) : (
          <p>No tournaments found for the selected filter.</p>
        )}
      </div>
    </div>
  );
};

export default ClubDashboard;
