import { Search } from 'lucide-react'
import { Input } from "../components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import ClubCard from '../components/ClubCard'

export default function HomePage() {
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
          <h2 className="text-xl lg:text-2xl font-bold">38 soccer clubs</h2>
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
        <ClubCard
          name="Arsenal Gunners FC"
          description="Saturday Casual Players"
          members={12}
          image="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/soccer-field-1-Rl9Hy7Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5.jpg"
          applied={true}
        />
        <ClubCard
          name="Bayern Munich FC"
          description="Beer And Soccer"
          members={6}
          image="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/soccer-field-2-Rl9Hy7Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5.jpg"
          applied={false}
        />
        <ClubCard
          name="Real Madrid CF"
          description="Champions League Enthusiasts"
          members={15}
          image="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/soccer-field-3-Rl9Hy7Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5Ue5.jpg"
          applied={false}
        />
      </div>
    </>
  )
}