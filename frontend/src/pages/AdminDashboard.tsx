import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../store';
import { User, Trophy, Users, Menu, X, Search, Bell } from 'lucide-react';
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { toast, Toaster } from 'react-hot-toast';
import { fetchAllPlayersAsync, selectPlayers } from '../store/userSlice';
import { fetchClubsAsync } from '../store/clubSlice';
import { fetchTournamentsAsync } from '../store/tournamentSlice';
import { selectUserId, clearUser } from '../store/userSlice';
import ClubCard from '../components/ClubCard';
import TournamentCard from '../components/TournamentCard';
import { Club } from '../types/club';
import { Tournament } from '../types/tournament';
import { PlayerProfile } from '../types/profile'; 
import PlayerProfileCard from '../components/PlayerProfileCard';
import { AvatarImage } from '../components/ui/avatar';
import { Link } from 'react-router-dom';

enum TournamentFilter {
  UPCOMING = 'All Tournaments',
  CURRENT = 'Pending',
  PAST = 'Verified',
  REJECTED = 'Rejected',
}

enum ClubFilter {
  ALL = 'All Clubs',
  REPORTED = 'Reported',
  BLACKLISTED = 'Blacklisted',
}

enum PlayerFilter {
  ALL = 'All Players',
  REPORTED = 'Reported',
  BLACKLISTED = 'Blacklisted',
}

const AdminDashboard = () => {
  const dispatch = useDispatch<AppDispatch>();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('players');
  const [searchTerm, setSearchTerm] = useState('');
  const [tournamentFilter, setTournamentFilter] = useState<TournamentFilter>(TournamentFilter.UPCOMING);
  const [clubFilter, setClubFilter] = useState<ClubFilter>(ClubFilter.ALL);
  const [playerFilter, setPlayerFilter] = useState<PlayerFilter>(PlayerFilter.ALL);

  const userId = useSelector(selectUserId);
  const players = useSelector(selectPlayers); // Use selectPlayers to access players
  const { clubs } = useSelector((state: RootState) => state.clubs);
  const { tournaments } = useSelector((state: RootState) => state.tournaments);

  useEffect(() => {
    dispatch(fetchClubsAsync());
    dispatch(fetchTournamentsAsync());
    dispatch(fetchAllPlayersAsync());
  }, [dispatch]);

  const filteredClubs = clubs.filter(club =>
    club.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredTournaments = tournaments.filter(tournament =>
    tournament.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredPlayers = players.filter((player: PlayerProfile) =>
    player.user.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    dispatch(clearUser());
    toast('You have been logged out.');
  };

  const NavItem = ({ to, icon: Icon, children }: { to: string; icon: React.ElementType; children: React.ReactNode }) => (
    <button
      className={`flex items-center space-x-2 p-2 rounded-md transition-colors duration-200 w-full text-left ${
        activeTab === to ? '!bg-gray-800 !text-white' : 'bg-transparent text-gray-300 hover:bg-gray-800 hover:text-white'
      }`}
      onClick={() => {
        setActiveTab(to);
        setIsSidebarOpen(false);
      }}
    >
      <Icon className="h-5 w-5" />
      <span>{children}</span>
    </button>
  );

  return (
    <div className="flex flex-col min-h-screen text-white">
      {/* Header */}
      <header className="flex justify-between items-center p-4">
        <div className="flex items-center">
          <Button
            size="icon"
            className="md:hidden mr-2"
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
          >
            {isSidebarOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </Button>
          <Link to="/" className="text-2xl font-bold ml-2 text-white hover:text-gray-300 transition-colors">
            KICKOFF Admin
          </Link>
        </div>
        {userId && (
          <div className="flex items-center space-x-4">
            <Button variant="ghost" className="relative" onClick={() => {}}>
              <Bell className="h-6 w-6 text-blue-500" />
            </Button>
            <Button variant="ghost" onClick={handleLogout}>
              Logout
            </Button>
            <AvatarImage
              src={`https://picsum.photos/seed/${userId + 2000}/100/100`}
              alt="User avatar"
            />
          </div>
        )}
      </header>

      {/* Main Content */}
      <div className="flex flex-1 overflow-hidden">
        {/* Sidebar */}
        <aside
          className={`fixed top-0 left-0 z-20 h-full w-64 p-6 space-y-6 transition-transform duration-300 ease-in-out transform
            ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full'}
            md:relative md:translate-x-0`}
        >
          <nav className="space-y-4">
            <NavItem to="players" icon={User}>Players</NavItem>
            <NavItem to="clubs" icon={Users}>Clubs</NavItem>
            <NavItem to="tournaments" icon={Trophy}>Tournaments</NavItem>
          </nav>
        </aside>

        <main className="flex-1 p-4 lg:p-6 overflow-auto w-full">
          <Toaster />
          <div className="mb-4">
            <h2 className="text-2xl font-bold mb-2">
              {activeTab === 'players' && 'Manage Players'}
              {activeTab === 'clubs' && 'Manage Clubs'}
              {activeTab === 'tournaments' && 'Manage Tournaments'}
            </h2>
            <div className="w-full mb-4">
              <div className="relative w-full">
                <Search className="absolute items-center left-2 top-2.5 w-4 text-gray-500" />
                <Input
                  type="search"
                  placeholder={`Search ${activeTab}`}
                  className="pl-8 bg-gray-800 border-gray-700 w-full"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
            </div>

            {/* Filters based on active tab */}
            {activeTab === 'tournaments' && (
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
            )}

            {activeTab === 'clubs' && (
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
            )}

            {activeTab === 'players' && (
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
            )}
          </div>

          {/* Club and Tournament Content */}
          {activeTab === 'clubs' && (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredClubs.map((club: Club) => (
                <div key={club.id}>
                  <ClubCard
                    id={club.id}
                    name={club.name}
                    description={club.clubDescription || 'No description available.'}
                    ratings={`ELO: ${club.elo.toFixed(0)}, RD: ${club.ratingDeviation.toFixed(0)}`}
                    image={`https://picsum.photos/seed/${club.id}/400/300`}
                    applied={false}
                    onClick={() => {}}
                  />
                </div>
              ))}
            </div>
          )}

          {activeTab === 'tournaments' && (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredTournaments.map((tournament: Tournament) => (
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
                  <Button onClick={() => {}}>
                    Manage Tournament
                  </Button>
                </TournamentCard>
              ))}
            </div>
          )}
        </main>
      </div>
    </div>
  );
};

export default AdminDashboard;
