import { useState, useEffect } from 'react'
import { Search } from 'lucide-react'
import { Input } from "../components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import { Button } from "../components/ui/button"
import axios from 'axios'
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
  const [tournaments, setTournaments] = useState<Tournament[]>([])
  const [filteredTournaments, setFilteredTournaments] = useState<Tournament[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [teamSizeFilter, setTeamSizeFilter] = useState<string | null>(null)
  const [knockoutFormatFilter, setKnockoutFormatFilter] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await axios.get('http://localhost:8080/tournaments', {
          auth: {
            username: 'admin',
            password: 'password'
          },
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          }
        });
        setTournaments(response.data);
        setFilteredTournaments(response.data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching tournaments:', err);
        setError('Failed to fetch tournaments');
        setLoading(false);
      }
    };

    fetchTournaments();
  }, []);

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

  if (loading) return <div>Loading...</div>
  if (error) return <div>Error: {error}</div>

  return (
    <>
      <Toaster />
      {/* Search and actions */}
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center mb-6 space-y-4 lg:space-y-0">
        <div className="relative w-full lg:w-64">
          <Search className="absolute left-2 top-2.5 h-4 w-4 text-gray-500" />
          <Input
            type="search"
            placeholder="Search tournaments"
            className="pl-8 bg-gray-800 border-gray-700 w-full"
            value={searchTerm}
            onChange={handleSearch}
          />
        </div>
      </div>

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

      {/* Filters */}
      <div className="flex flex-col lg:flex-row justify-end space-y-2 lg:space-y-0 lg:space-x-4 mb-6">
        <Select onValueChange={handleTeamSizeFilter}>
          <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
            <SelectValue placeholder="Team Size" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="ALL" displayValue="All Sizes">All Sizes</SelectItem>
            <SelectItem value="FIVE_SIDE" displayValue="Five-a-side">Five-a-side</SelectItem>
            <SelectItem value="SEVEN_SIDE" displayValue="Seven-a-side">Seven-a-side</SelectItem>
          </SelectContent>
        </Select>
        <Select onValueChange={handleKnockoutFormatFilter}>
          <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
            <SelectValue placeholder="Knockout Format" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="ALL" displayValue="All Formats">All Formats</SelectItem>
            <SelectItem value="SINGLE_ELIM" displayValue="Single Elimination">Single Elimination</SelectItem>
            <SelectItem value="DOUBLE_ELIM" displayValue="Double Elimination">Double Elimination</SelectItem>
          </SelectContent>
        </Select>
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
            <Button onClick={() => console.log(`Join tournament ${tournament.id}`)}>Join</Button>
          </TournamentCard>
        ))}
      </div>
    </>
  )
}