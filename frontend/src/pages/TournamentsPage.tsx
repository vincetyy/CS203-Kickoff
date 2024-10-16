import { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { fetchTournamentsAsync, joinTournamentAsync, removeClubFromTournamentAsync } from '../store/tournamentSlice'
import { AppDispatch, RootState } from '../store'
import { Search } from 'lucide-react'
import { Input } from "../components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import { Button } from "../components/ui/button"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog"
import { toast } from 'react-hot-toast'
import TournamentCard from '../components/TournamentCard'
import CreateTournament from '../components/CreateTournament'
import { Tournament } from '../types/tournament'
import { fetchUserClubAsync, selectUserId } from '../store/userSlice'


export default function TournamentsPage() {
  const dispatch = useDispatch<AppDispatch>()
  const { userClub } = useSelector((state: RootState) => state.user);
  const userId = useSelector(selectUserId);
  const { tournaments, status, error } = useSelector((state: RootState) => state.tournaments)
  const [filteredTournaments, setFilteredTournaments] = useState<Tournament[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [teamSizeFilter, setTeamSizeFilter] = useState<string | null>(null)
  const [knockoutFormatFilter, setKnockoutFormatFilter] = useState<string | null>(null)
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [isLeaveDialogOpen, setIsLeaveDialogOpen] = useState(false)
  const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null)
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)

  let isCaptain = false;
  
  if (userClub) {
    isCaptain = userClub?.captainId === userId;
  }

  useEffect(() => {
    dispatch(fetchTournamentsAsync())
    dispatch(fetchUserClubAsync());
  }, [dispatch])

  useEffect(() => {
    console.log(tournaments);
    
    let results = tournaments.filter(tournament =>
      tournament.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    if (teamSizeFilter) {
      results = results.filter(tournament => tournament.tournamentFormat === teamSizeFilter);
    }

    if (knockoutFormatFilter) {
      results = results.filter(tournament => tournament.knockoutFormat === knockoutFormatFilter);
    }

    setFilteredTournaments(results);
  }, [searchTerm, teamSizeFilter, knockoutFormatFilter, tournaments]);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const handleTeamSizeFilter = (value: string) => {
    setTeamSizeFilter(value === 'ALL' ? null : value);
  };

  const handleKnockoutFormatFilter = (value: string) => {
    setKnockoutFormatFilter(value === 'ALL' ? null : value);
  };

  const handleJoin = (tournament: Tournament) => {
    setSelectedTournament(tournament)
    setIsDialogOpen(true)
  };

  const handleLeave = (tournament: Tournament) => {
    setSelectedTournament(tournament)
    setIsLeaveDialogOpen(true)
  };

  const handleConfirmJoin = async () => {
    if (!selectedTournament) return

    if (!userClub) {
      toast.error("User club information is missing.", {
        duration: 4000,
        position: 'top-center',
      });
      return;
    }

    /**
     * Works for now because we only have 5 or 7 a side
     * but need to change in the future with more 
     * formats or team sizes
     */
    const requiredPlayers = selectedTournament.tournamentFormat === "FIVE_SIDE" ? 5 : 7; // Adjust based on your formats
    const currentPlayerCount = userClub.players ? userClub.players.length : 0;

    if (currentPlayerCount < requiredPlayers) {
      toast.error(`You need at least ${requiredPlayers} players to join this tournament. Currently, you have ${currentPlayerCount} players.`, {
        duration: 4000,
        position: 'top-center',
      });
      return; // Prevent the API call
    }

    try {
      const result = await dispatch(joinTournamentAsync({ 
        clubId: userClub.id, // Hardcoded club ID
        tournamentId: selectedTournament.id 
      })).unwrap()
      
      // Close the dialog first
      setIsDialogOpen(false)
      setSelectedTournament(null)

      // Show the success toast
      toast.success(`Successfully joined ${selectedTournament.name}`, {
        duration: 3000,
        position: 'top-center',
      })

      // Update the specific tournament in the state
      const updatedTournaments = tournaments.map(t => 
        t.id === selectedTournament.id ? { ...t, joinedClubsIds: [...(t.joinedClubsIds || []), userClub.id ] } : t
      );
      dispatch({ type: 'tournaments/updateTournaments', payload: updatedTournaments });

    } catch (err: any) {
      console.error('Error joining tournament:', err)
      toast.error(`${err.message}`, {
        duration: 4000,
        position: 'top-center',
      })
    }
  };

  const handleConfirmLeave = async () => {

    
    if (!selectedTournament || !userClub) return
    try {
      await dispatch(removeClubFromTournamentAsync({ 
        tournamentId: selectedTournament.id, 
        clubId: userClub.id 
      })).unwrap();

      // Close the dialog first
      setIsLeaveDialogOpen(false)
      setSelectedTournament(null)

      // Show the success toast
      toast.success(`Successfully left ${selectedTournament.name}`, {
        duration: 3000,
        position: 'top-center',
      })

      // Update the specific tournament in the state
      const updatedTournaments = tournaments.map(t => 
        t.id === selectedTournament.id 
          ? { 
              ...t, 
              joinedClubsIds: (t.joinedClubsIds || []).filter(club => club !== userClub.id) 
            }
          : t
      );
      dispatch({ type: 'tournaments/updateTournaments', payload: updatedTournaments });

    } catch (err: any) {
      console.error('Error leaving tournament:', err)
      toast.error(`${err.message}`, {
        duration: 4000,
        position: 'top-center',
      })
    }
  };

  if (status === 'loading') return <div>Loading...</div>
  if (status === 'failed') return <div>Error: {error}</div>

  return (
    <>
      {/* Banner */}
      <div className="bg-blue-600 rounded-lg p-4 lg:p-6 mb-6 flex items-center space-x-4">
        <div className="bg-yellow-400 rounded-full p-2 lg:p-3">
          <svg className="h-6 w-6 lg:h-8 lg:w-8 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
        </div>
        <div>
          <h2 className="text-xl lg:text-2xl font-bold">{filteredTournaments.length} tournaments</h2>
          <p className="text-sm lg:text-base">Join the competition</p>
        </div>
      </div>

      {/* Search, Filters, and Create Button */}
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center space-y-2 lg:space-y-0 lg:space-x-4 mb-6">
        <div className="flex flex-col lg:flex-row space-y-2 lg:space-y-0 lg:space-x-4 w-full lg:w-auto">
          <div className="relative w-full lg:w-[300px]">
            <Search className="absolute left-2 top-2.5 h-4 w-4 text-gray-500" />
            <Input
              type="search"
              placeholder="Search tournaments"
              className="pl-8 bg-gray-800 border-gray-700 w-full h-10"
              value={searchTerm}
              onChange={handleSearch}
            />
          </div>
          <Select onValueChange={handleTeamSizeFilter}>
            <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
              <SelectValue placeholder="Team Size" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">All Sizes</SelectItem>
              <SelectItem value="FIVE_SIDE">Five-a-side</SelectItem>
              <SelectItem value="SEVEN_SIDE">Seven-a-side</SelectItem>
            </SelectContent>
          </Select>
          <Select onValueChange={handleKnockoutFormatFilter}>
            <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
              <SelectValue placeholder="Knockout Format" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">All Formats</SelectItem>
              <SelectItem value="SINGLE_ELIM">Single Elimination</SelectItem>
              <SelectItem value="DOUBLE_ELIM">Double Elimination</SelectItem>
            </SelectContent>
          </Select>
        </div>
        {
          userId &&
          <Button onClick={() => setIsCreateDialogOpen(true)} className="bg-blue-600 hover:bg-blue-700 w-full lg:w-auto">
            Create Tournament
          </Button>
        }
      </div>

      {/* Tournament cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 lg:gap-6">
        {filteredTournaments.map((tournament) => {
          const isUserClubInTournament = tournament.joinedClubsIds?.includes(userClub?.id);

          return (
            <TournamentCard
              key={tournament.id}
              id={tournament.id}
              name={tournament.name}
              startDate={new Date(tournament.startDateTime).toLocaleDateString()}
              endDate={new Date(tournament.endDateTime).toLocaleDateString()}
              format={tournament.tournamentFormat}
              teams={`${tournament.joinedClubsIds?.length || 0}/${tournament.maxTeams}`}  // Ensure joinedClubs is defined
              image={`https://picsum.photos/seed/${tournament.id + 1000}/400/300`}
            >
              {userClub && isCaptain && (
                <>
                  {isUserClubInTournament ? (
                    <Button onClick={() => handleLeave(tournament)}
                    className="bg-red-500 hover:bg-red-600 text-white">Leave</Button>
                  ) : (
                    <Button onClick={() => handleJoin(tournament)}>Join</Button>
                  )}
                </>
              )}
            </TournamentCard>
          );
        })}
      </div>

      {/* Join confirmation dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Join {selectedTournament?.name}</DialogTitle>
          </DialogHeader>
          <div className="mt-4">
            <p>Are you sure you want to join this tournament?</p>
          </div>
          <div className="flex flex-col sm:flex-row justify-between mt-4 space-y-2 sm:space-y-0 sm:space-x-2">
            <button 
              className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 w-full" 
              onClick={() => setIsDialogOpen(false)}
            >
              Cancel
            </button>
            <button 
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 w-full"
              onClick={handleConfirmJoin}
            >
              Confirm
            </button>
          </div>
        </DialogContent>
      </Dialog>

      {/* Leave confirmation dialog */}
      <Dialog open={isLeaveDialogOpen} onOpenChange={setIsLeaveDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Leave {selectedTournament?.name}</DialogTitle>
          </DialogHeader>
          <div className="mt-4">
            <p>Are you sure you want to leave this tournament?</p>
          </div>
          <div className="flex flex-col sm:flex-row justify-between mt-4 space-y-2 sm:space-y-0 sm:space-x-2">
            <button 
              className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 w-full" 
              onClick={() => setIsLeaveDialogOpen(false)}
            >
              Cancel
            </button>
            <button 
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 w-full"
              onClick={handleConfirmLeave}
            >
              Confirm
            </button>
          </div>
        </DialogContent>
      </Dialog>

      {/* Create Tournament Dialog */}
      <CreateTournament isOpen={isCreateDialogOpen} onClose={setIsCreateDialogOpen} />
    </>
  )
}
