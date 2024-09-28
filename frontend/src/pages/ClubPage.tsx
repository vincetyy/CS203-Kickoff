import { useState, useEffect } from 'react'
import { Search } from 'lucide-react'
import { Input } from "../components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import ClubCard from '../components/ClubCard'
import axios from 'axios'

// Define an interface for the Club data structure
interface Club {
  id: number
  name: string
  description?: string
  players: { id: number }[]
  elo: number
  ratingDeviation: number
}

export default function ClubPage() {
  const [clubs, setClubs] = useState<Club[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

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
        setLoading(false);
      } catch (err) {
        console.error('Error fetching clubs:', err);
        setError('Failed to fetch clubs');
        setLoading(false);
      }
    };

    fetchClubs();
  }, []);

  if (loading) return <div>Loading...</div>
  if (error) return <div>Error: {error}</div>

  return (
    <>
      {/* Search and actions */}
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center mb-6 space-y-4 lg:space-y-0">
        <div className="relative w-full lg:w-64">
          <Search className="absolute left-2 top-2.5 h-4 w-4 text-gray-500" />
          <Input type="search" placeholder="Search" className="pl-8 bg-gray-800 border-gray-700 w-full" />
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
          <h2 className="text-xl lg:text-2xl font-bold">{clubs.length} soccer clubs</h2>
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
            <SelectItem value="casual">Casual</SelectItem>
            <SelectItem value="competitive">Competitive</SelectItem>
            <SelectItem value="friendly">Friendly</SelectItem>
          </SelectContent>
        </Select>
        <Select>
          <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
            <SelectValue placeholder="Location" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="singapore">Singapore</SelectItem>
            <SelectItem value="malaysia">Malaysia</SelectItem>
            <SelectItem value="indonesia">Indonesia</SelectItem>
          </SelectContent>
        </Select>
        <Select>
          <SelectTrigger className="w-full lg:w-[180px] bg-gray-800 border-gray-700 text-white">
            <SelectValue placeholder="Skill Level" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="beginner">Beginner</SelectItem>
            <SelectItem value="intermediate">Intermediate</SelectItem>
            <SelectItem value="advanced">Advanced</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* Club cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 lg:gap-6">
        {clubs.map((club) => (
          <ClubCard
            key={club.id}
            name={club.name}
            description={club.description || `ELO: ${club.elo.toFixed(0)}, RD: ${club.ratingDeviation.toFixed(0)}`}
            members={club.players.length}
            image={`https://picsum.photos/seed/${club.id}/400/300`} // Using a placeholder image service
            applied={false} // You might want to implement this logic based on user state
          />
        ))}
      </div>
    </>
  )
}