import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTournamentsAsync } from '../store/tournamentSlice';
import { Tournament } from '../types/tournament';
import TournamentCard from '../components/TournamentCard';
import { Input } from "../components/ui/input";
import { Search } from 'lucide-react';
import { Button } from "../components/ui/button";
import { AppDispatch, RootState } from '../store'; 

const AdminTournament = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { tournaments } = useSelector((state: RootState) => state.tournaments);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    dispatch(fetchTournamentsAsync());
  }, [dispatch]);

  const filteredTournaments = tournaments.filter((tournament: Tournament) =>
    tournament.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Manage Tournaments</h2>
      <div className="relative w-full mb-4">
        <Search className="absolute left-2 top-2.5 h-4 w-4 text-gray-500" />
        <Input
          type="search"
          placeholder="Search Tournaments"
          className="pl-8 bg-gray-800 border-gray-700 w-full"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredTournaments.length > 0 ? (
          filteredTournaments.map((tournament: Tournament) => (
            <TournamentCard
              key={tournament.id}
              id={tournament.id || 0}
              name={tournament.name}
              startDate={new Date(tournament.startDateTime).toLocaleDateString()}
              endDate={new Date(tournament.endDateTime).toLocaleDateString()}
              format={tournament.tournamentFormat}
              teams={`${tournament.joinedClubsIds?.length || 0}/${tournament.maxTeams}`}
              image={`https://picsum.photos/seed/${tournament.id + 1000}/400/300`}
            >
              <Button onClick={() => {}}>Manage Tournament</Button>
            </TournamentCard>
          ))
        ) : (
          <p>No tournaments available</p>
        )}
      </div>
    </div>
  );
};

export default AdminTournament;