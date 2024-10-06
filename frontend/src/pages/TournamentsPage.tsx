import { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { fetchTournamentsAsync, joinTournamentAsync, createTournamentAsync } from '../store/tournamentSlice'
import { AppDispatch, RootState } from '../store'
import { Search } from 'lucide-react'
import { Input } from "../components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import { Button } from "../components/ui/button"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog"
import { Toaster, toast } from 'react-hot-toast'
import TournamentCard from '../components/TournamentCard'

interface Location {
  id: number
  name: string
}

interface Tournament {
  id: number
  name: string
  startDateTime: string
  endDateTime: string
  location: Location
  maxTeams: number
  tournamentFormat: string
  knockoutFormat: string
  minRank: number | null
  maxRank: number | null
  joinedClubs: any[] // You might want to define a more specific type for clubs
  over: boolean
}

export default function TournamentsPage() {
  const dispatch = useDispatch<AppDispatch>()
  const { tournaments, status, error } = useSelector((state: RootState) => state.tournaments)
  const [filteredTournaments, setFilteredTournaments] = useState<Tournament[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [teamSizeFilter, setTeamSizeFilter] = useState<string | null>(null)
  const [knockoutFormatFilter, setKnockoutFormatFilter] = useState<string | null>(null)
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null)
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [newTournament, setNewTournament] = useState({
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

  useEffect(() => {
    dispatch(fetchTournamentsAsync())
  }, [dispatch])

  useEffect(() => {
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
    setSelectedTournament(tournament);
    setIsDialogOpen(true);
  };

  const handleConfirmJoin = async () => {
    if (!selectedTournament) return;

    try {
      await dispatch(joinTournamentAsync({ 
        clubId: 1, // Hardcoded club ID
        tournamentId: selectedTournament.id 
      })).unwrap();
      toast.success(`Successfully joined ${selectedTournament.name}`, {
        duration: 3000,
        position: 'top-center',
      });
      setIsDialogOpen(false);
      setSelectedTournament(null);
      dispatch(fetchTournamentsAsync()); // Refresh tournaments after joining
    } catch (err) {
      console.error('Error joining tournament:', err);
      toast.error(`${(err as any).message}`, {
        duration: 4000,
        position: 'top-center',
      });
    }
  };

  const handleCreateTournament = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await dispatch(createTournamentAsync(newTournament)).unwrap();
      toast.success('Tournament created successfully!', {
        duration: 3000,
        position: 'top-center',
      })
      setIsCreateDialogOpen(false)
      dispatch(fetchTournamentsAsync()); // Refresh tournaments after creating
    } catch (err) {
      console.error('Error creating tournament:', err)
      toast.error(`Failed to create tournament: ${(err as any).message}`, {
        duration: 4000,
        position: 'top-center',
      })
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setNewTournament(prev => ({ ...prev, [name]: value }))
  }

  if (status === 'loading') return <div>Loading...</div>
  if (status === 'failed') return <div>Error: {error}</div>

  return (
    <>
      <Toaster />
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
        <Button onClick={() => setIsCreateDialogOpen(true)} className="bg-blue-600 hover:bg-blue-700 w-full lg:w-auto">
          Create Tournament
        </Button>
      </div>

      {/* Tournament cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 lg:gap-6">
        {filteredTournaments.map((tournament) => (
          <TournamentCard
            key={tournament.id}
            name={tournament.name}
            startDate={new Date(tournament.startDateTime).toLocaleDateString()}
            endDate={new Date(tournament.endDateTime).toLocaleDateString()}
            format={tournament.tournamentFormat}
            teams={`${tournament.joinedClubs.length}/${tournament.maxTeams}`}
            image={`https://picsum.photos/seed/${tournament.id}/400/300`}
          >
            <Button onClick={() => handleJoin(tournament)}>Join</Button>
          </TournamentCard>
        ))}
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

      {/* Create Tournament Dialog */}
      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent className="sm:max-w-[600px] lg:max-w-[800px]">
          <DialogHeader>
            <DialogTitle>Create New Tournament</DialogTitle>
          </DialogHeader>
          <form onSubmit={handleCreateTournament} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label htmlFor="name" className="block text-sm font-medium text-gray-300">Tournament Name</label>
                <Input
                  id="name"
                  name="name"
                  value={newTournament.name}
                  onChange={handleInputChange}
                  className="mt-1"
                  required
                />
              </div>
              <div>
                <label htmlFor="locationId" className="block text-sm font-medium text-gray-300">Location ID</label>
                <Input
                  id="locationId"
                  name="locationId"
                  type="number"
                  value={newTournament.locationId}
                  onChange={handleInputChange}
                  className="mt-1"
                  required
                />
              </div>
              <div>
                <label htmlFor="startDateTime" className="block text-sm font-medium text-gray-300">Start Date & Time</label>
                <Input
                  id="startDateTime"
                  name="startDateTime"
                  type="datetime-local"
                  value={newTournament.startDateTime}
                  onChange={handleInputChange}
                  className="mt-1"
                  required
                />
              </div>
              <div>
                <label htmlFor="endDateTime" className="block text-sm font-medium text-gray-300">End Date & Time</label>
                <Input
                  id="endDateTime"
                  name="endDateTime"
                  type="datetime-local"
                  value={newTournament.endDateTime}
                  onChange={handleInputChange}
                  className="mt-1"
                  required
                />
              </div>
              <div>
                <label htmlFor="maxTeams" className="block text-sm font-medium text-gray-300">Max Teams</label>
                <Input
                  id="maxTeams"
                  name="maxTeams"
                  type="number"
                  value={newTournament.maxTeams}
                  onChange={handleInputChange}
                  className="mt-1"
                  required
                />
              </div>
              <div>
                <label htmlFor="tournamentFormat" className="block text-sm font-medium text-gray-300">Tournament Format</label>
                <Select onValueChange={(value) => handleInputChange({ target: { name: 'tournamentFormat', value } } as any)}>
                  <SelectTrigger className="w-full mt-1">
                    <SelectValue placeholder="Select format" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="FIVE_SIDE">Five-a-side</SelectItem>
                    <SelectItem value="SEVEN_SIDE">Seven-a-side</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <label htmlFor="knockoutFormat" className="block text-sm font-medium text-gray-300">Knockout Format</label>
                <Select onValueChange={(value) => handleInputChange({ target: { name: 'knockoutFormat', value } } as any)}>
                  <SelectTrigger className="w-full mt-1">
                    <SelectValue placeholder="Select format" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="SINGLE_ELIM">Single Elimination</SelectItem>
                    <SelectItem value="DOUBLE_ELIM">Double Elimination</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <label htmlFor="minRank" className="block text-sm font-medium text-gray-300">Min Rank</label>
                <Input
                  id="minRank"
                  name="minRank"
                  type="number"
                  value={newTournament.minRank}
                  onChange={handleInputChange}
                  className="mt-1"
                />
              </div>
              <div>
                <label htmlFor="maxRank" className="block text-sm font-medium text-gray-300">Max Rank</label>
                <Input
                  id="maxRank"
                  name="maxRank"
                  type="number"
                  value={newTournament.maxRank}
                  onChange={handleInputChange}
                  className="mt-1"
                />
              </div>
            </div>
            <div className="flex justify-end space-x-2 mt-6">
              <Button type="button" onClick={() => setIsCreateDialogOpen(false)} className="bg-gray-600 hover:bg-gray-700">
                Cancel
              </Button>
              <Button type="submit" className="bg-blue-600 hover:bg-blue-700">
                Create
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </>
  )
}