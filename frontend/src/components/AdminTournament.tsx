import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTournamentsAsync } from '../store/tournamentSlice';
import { Tournament } from '../types/tournament';
import TournamentCard from '../components/TournamentCard';
import { Input } from "../components/ui/input";
import { Search } from 'lucide-react';
import { Button } from "../components/ui/button";
import { AppDispatch, RootState } from '../store';

enum TournamentFilter {
  UPCOMING = 'All Tournaments',
  CURRENT = 'Pending',
  PAST = 'Verified',
  REJECTED = 'Rejected',
}

const AdminTournament = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { tournaments } = useSelector((state: RootState) => state.tournaments);
  const [searchTerm, setSearchTerm] = useState('');
  const [tournamentFilter, setTournamentFilter] = useState<TournamentFilter>(TournamentFilter.UPCOMING);

  useEffect(() => {
    dispatch(fetchTournamentsAsync());
  }, [dispatch]);

  const filteredTournaments = tournaments.filter((tournament: Tournament) => {
    const matchesSearch = tournament.name.toLowerCase().includes(searchTerm.toLowerCase());

    // Apply tournament filter logic
    switch (tournamentFilter) {
      case TournamentFilter.UPCOMING:
        return matchesSearch; // Show all tournaments
      // case TournamentFilter.CURRENT:
      //   return matchesSearch && tournament.status === 'PENDING'; 
      // case TournamentFilter.PAST:
      //   return matchesSearch && tournament.status === 'VERIFIED'; 
      // case TournamentFilter.REJECTED:
      //   return matchesSearch && tournament.status === 'REJECTED'; 
      default:
        return false;
    }
  });

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

      {/* Filter Buttons */}
      <div className="flex justify-center space-x-4 mb-4">
        {Object.values(TournamentFilter).map((filter) => (
          <Button
            key={filter}
            onClick={() => setTournamentFilter(filter)}
            variant={tournamentFilter === filter ? "default" : "secondary"}
          >
            {filter}
          </Button>
        ))}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredTournaments.length > 0 ? (
          filteredTournaments.map((tournament: Tournament) => (
            tournament.id && 
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
              <Button className="bg-blue-500 hover:bg-blue-600 w-32 h-10">
                Manage Tournament
              </Button>
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
