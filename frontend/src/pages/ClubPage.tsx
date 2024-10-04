import { useState, useEffect } from 'react'
import { Search } from 'lucide-react'
import { Input } from "../components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog"
import { Button } from "../components/ui/button"
import ClubCard from '../components/ClubCard'
import axios from 'axios'
import { Toaster, toast } from 'react-hot-toast'

interface Club {
  id: number
  name: string
  description?: string
  players: { id: number }[]
  elo: number
  ratingDeviation: number
}

enum PlayerPosition {
  POSITION_FORWARD = "POSITION_FORWARD",
  POSITION_MIDFIELDER = "POSITION_MIDFIELDER",
  POSITION_DEFENDER = "POSITION_DEFENDER",
  POSITION_GOALKEEPER = "POSITION_GOALKEEPER"
}

export default function ClubPage() {
  const [clubs, setClubs] = useState<Club[]>([])
  const [filteredClubs, setFilteredClubs] = useState<Club[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedClub, setSelectedClub] = useState<Club | null>(null)
  const [selectedPosition, setSelectedPosition] = useState<PlayerPosition | null>(null)
  const [isDialogOpen, setIsDialogOpen] = useState(false)

  useEffect(() => {
    const fetchClubs = async () => {
      try {
        const response = await axios.get('http://localhost:8080/clubs', {
          auth: {
            username: 'admin',
            password: 'password'
          },
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          }
        });
        setClubs(response.data);
        setFilteredClubs(response.data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching clubs:', err);
        setError('Failed to fetch clubs');
        setLoading(false);
      }
    };

    fetchClubs();
  }, []);

  useEffect(() => {
    const results = clubs.filter(club =>
      club.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      (club.description && club.description.toLowerCase().includes(searchTerm.toLowerCase()))
    );
    setFilteredClubs(results);
  }, [searchTerm, clubs]);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const formatPosition = (position: string) => {
    return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
  }
  const handleJoin = (club: Club) => {
    setSelectedClub(club)
    setIsDialogOpen(true)
  }

  const handleApply = async () => {
    if (!selectedClub || !selectedPosition) return;

    try {
      await axios.post(`http://localhost:8080/clubs/${selectedClub.id}/apply`, 
        { 
          playerProfileId: 1, // Assuming a fixed player profile ID for now
          desiredPosition: selectedPosition 
        },
        {
          auth: {
            username: 'admin',
            password: 'password'
          },
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          }
        }
      );
      toast.success(`Successfully applied to ${selectedClub.name} as ${selectedPosition.replace('POSITION_', '')}`, {
        duration: 3000,
        position: 'top-center',
      });
      setIsDialogOpen(false);
      setSelectedClub(null);
      setSelectedPosition(null);
    } catch (err) {
      console.error('Error applying to club:', err);
      toast.error(`${(err as any).response?.data || (err as Error).message}`, {
        duration: 4000,
        position: 'top-center',
      });
    }
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
            placeholder="Search"
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
          <h2 className="text-xl lg:text-2xl font-bold">{filteredClubs.length} soccer clubs</h2>
          <p className="text-sm lg:text-base">waiting for you</p>
        </div>
      </div>

      {/* Filters */}
      <div className="flex flex-col lg:flex-row justify-end space-y-2 lg:space-y-0 lg:space-x-4 mb-6">
        <Select>
          <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
            <SelectValue placeholder="Type" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="Casual">Casual</SelectItem>
            <SelectItem value="Competitive">Competitive</SelectItem>
            <SelectItem value="Friendly">Friendly</SelectItem>
          </SelectContent>
        </Select>
        <Select>
          <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
            <SelectValue placeholder="Location" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="Singapore">Singapore</SelectItem>
            <SelectItem value="Malaysia">Malaysia</SelectItem>
            <SelectItem value="Indonesia">Indonesia</SelectItem>
          </SelectContent>
        </Select>
        <Select>
          <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
            <SelectValue placeholder="Skill Level" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="Beginner">Beginner</SelectItem>
            <SelectItem value="Intermediate">Intermediate</SelectItem>
            <SelectItem value="Advanced">Advanced</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* Club cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 lg:gap-6">
        {filteredClubs.map((club) => (
          <ClubCard
            key={club.id}
            name={club.name}
            description={club.description || `ELO: ${club.elo.toFixed(0)}, RD: ${club.ratingDeviation.toFixed(0)}`}
            members={club.players.length}
            image={`https://picsum.photos/seed/${club.id}/400/300`}
            applied={false}
          >
            <Button onClick={() => handleJoin(club)}>Join</Button>
          </ClubCard>
        ))}
      </div>

      {/* Position selection dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Apply to {selectedClub?.name}</DialogTitle>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="flex flex-col justify-between">
              <Select onValueChange={(value) => setSelectedPosition(value as PlayerPosition)}>
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Select your preferred position" />
                </SelectTrigger>
                <SelectContent>
                  {Object.values(PlayerPosition).map((position) => (
                    <SelectItem key={position} value={position}>
                      {formatPosition(position)}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
          <div className="flex flex-col sm:flex-row justify-between mt-4 space-y-2 sm:space-y-0 sm:space-x-2">
            <button 
              className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 w-full" 
              onClick={() => setIsDialogOpen(false)}
            >
              Close
            </button>
            <button 
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 w-full"
              onClick={handleApply}
              disabled={!selectedPosition}
            >
              Apply
            </button>
          </div>
        </DialogContent>
      </Dialog>
    </>
  )
}