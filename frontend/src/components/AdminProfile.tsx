import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchAllPlayersAsync, selectPlayers } from '../store/userSlice';
import { PlayerProfile } from '../types/profile';
import PlayerProfileCard from '../components/PlayerProfileCard';
import { Input } from "../components/ui/input";
import { Search } from 'lucide-react';
import { AppDispatch } from '../store'; 
import { Button } from "./ui/button";

enum PlayerFilter {
  ALL = 'All Players',
  REPORTED = 'Reported',
  BLACKLISTED = 'Blacklisted',
}

const AdminProfile = () => {
  const dispatch = useDispatch<AppDispatch>();
  const players = useSelector(selectPlayers);
  const [searchTerm, setSearchTerm] = useState('');
  const [playerFilter, setPlayerFilter] = useState<PlayerFilter>(PlayerFilter.ALL);

  useEffect(() => {
    dispatch(fetchAllPlayersAsync());
  }, [dispatch]);

  const filteredPlayers = players.filter((player: PlayerProfile) => {
    const matchesSearch = player.user.username.toLowerCase().includes(searchTerm.toLowerCase());

    // Apply player filter logic
    if (playerFilter === PlayerFilter.ALL) return matchesSearch;
    // if (playerFilter === PlayerFilter.REPORTED) return matchesSearch && player.isReported; 
    // if (playerFilter === PlayerFilter.BLACKLISTED) return matchesSearch && player.isBlacklisted; 
    return false;
  });

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Manage Players</h2>
      <div className="relative w-full mb-4">
        <Search className="absolute left-2 top-2.5 h-4 w-4 text-gray-500" />
        <Input
          type="search"
          placeholder="Search Players"
          className="pl-8 bg-gray-800 border-gray-700 w-full"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      {/* Filter Buttons */}
      <div className="flex justify-center space-x-4 mb-4">
        {Object.values(PlayerFilter).map((filter) => (
          <Button
            key={filter}
            onClick={() => setPlayerFilter(filter)}
            variant={playerFilter === filter ? "default" : "secondary"}
          >
            {filter}
          </Button>
        ))}
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredPlayers.length > 0 ? (
          filteredPlayers.map((player: PlayerProfile) => (
            <PlayerProfileCard
              key={player.id}
              id={player.id}
              availability={true}  
              needAvailability={true}  
            />
          ))
        ) : (
          <p>No players available</p>
        )}
      </div>
    </div>
  );
};

export default AdminProfile;
