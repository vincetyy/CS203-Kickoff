import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchAllPlayersAsync, selectPlayers } from '../store/userSlice';
import { PlayerProfile } from '../types/profile';
import PlayerProfileCard from '../components/PlayerProfileCard';
import { Input } from "../components/ui/input";
import { Search } from 'lucide-react';
import { AppDispatch, RootState } from '../store'; 

const AdminProfile = () => {
    const dispatch = useDispatch<AppDispatch>();
  const players = useSelector(selectPlayers);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    dispatch(fetchAllPlayersAsync());
  }, [dispatch]);

  const filteredPlayers = players.filter((player: PlayerProfile) =>
    player.user.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

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