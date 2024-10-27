import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchClubsAsync } from '../store/clubSlice';
import { Club } from '../types/club';
import ClubCard from '../components/ClubCard';
import { Input } from "../components/ui/input";
import { Search } from 'lucide-react';
import { AppDispatch, RootState } from '../store'; 
import { Button } from "./ui/button";

enum ClubFilter {
  ALL = 'All Clubs',
  REPORTED = 'Reported',
  BLACKLISTED = 'Blacklisted',
}

const AdminClub = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { clubs } = useSelector((state: RootState) => state.clubs);
  const [searchTerm, setSearchTerm] = useState('');
  const [clubFilter, setClubFilter] = useState<ClubFilter>(ClubFilter.ALL);

  useEffect(() => {
    dispatch(fetchClubsAsync());
  }, [dispatch]);

  const filteredClubs = clubs.filter((club: Club) => {
    const matchesSearch = club.name.toLowerCase().includes(searchTerm.toLowerCase());

    // Apply club filter logic
    if (clubFilter === ClubFilter.ALL) return matchesSearch;
    // if (clubFilter === ClubFilter.REPORTED) return matchesSearch && club.isReported; 
    // if (clubFilter === ClubFilter.BLACKLISTED) return matchesSearch && club.isBlacklisted; 
    return false;
  });

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Manage Clubs</h2>
      <div className="relative w-full mb-4">
        <Search className="absolute left-2 top-2.5 h-4 w-4 text-gray-500" />
        <Input
          type="search"
          placeholder="Search Clubs"
          className="pl-8 bg-gray-800 border-gray-700 w-full"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      {/* Filter Buttons */}
      <div className="flex justify-center space-x-4 mb-4">
        {Object.values(ClubFilter).map((filter) => (
          <Button
            key={filter}
            onClick={() => setClubFilter(filter)}
            variant={clubFilter === filter ? "default" : "secondary"}
          >
            {filter}
          </Button>
        ))}
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredClubs.length > 0 ? (
          filteredClubs.map((club: Club) => (
            <ClubCard
              key={club.id}
              id={club.id}
              name={club.name}
              description={club.clubDescription || 'No description available.'}
              ratings={`ELO: ${club.elo.toFixed(0)}, RD: ${club.ratingDeviation.toFixed(0)}`}
              image={`https://picsum.photos/seed/${club.id}/400/300`}
              applied={false}
              onClick={() => {}}
            />
          ))
        ) : (
          <p>No clubs available</p>
        )}
      </div>
    </div>
  );
};

export default AdminClub;
