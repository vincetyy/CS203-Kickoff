import { NavLink } from 'react-router-dom'
import { User, Trophy, Users, BarChart2 } from 'lucide-react'

export default function Sidebar() {
  return (
    <aside className="w-64 h-full bg-gray-900 p-6 space-y-6">
      <nav className="space-y-4">
        <NavLink to="/profile" className="flex items-center space-x-2 text-gray-300 hover:text-white">
          <User className="h-5 w-5" />
          <span>Profile</span>
        </NavLink>
        <NavLink to="/tournaments" className="flex items-center space-x-2 text-gray-300 hover:text-white">
          <Trophy className="h-5 w-5" />
          <span>Tournaments</span>
        </NavLink>
        <NavLink to="/club" className="flex items-center space-x-2 text-gray-300 hover:text-white">
          <Users className="h-5 w-5" />
          <span>Club</span>
        </NavLink>
        <NavLink to="/leaderboards" className="flex items-center space-x-2 text-gray-300 hover:text-white">
          <BarChart2 className="h-5 w-5" />
          <span>Leaderboards</span>
        </NavLink>
      </nav>
    </aside>
  )
}