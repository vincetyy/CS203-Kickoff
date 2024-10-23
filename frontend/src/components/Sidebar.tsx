import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { User, Trophy, Users, Menu, X } from 'lucide-react';

interface SidebarProps {
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

export default function Sidebar({ isOpen, setIsOpen }: SidebarProps) {
  const toggleSidebar = () => setIsOpen(!isOpen);

  // Get the current location
  const location = useLocation();

  // Custom NavItem component with active state
  const NavItem = ({ to, icon: Icon, children }: { to: string; icon: React.ElementType; children: React.ReactNode }) => (
    <NavLink
      to={to}
      className={({ isActive }) =>
        `flex items-center space-x-2 p-2 rounded-md transition-colors duration-200 ${
          // Check if current path starts with `to` to apply active state
          location.pathname.startsWith(to) || isActive
            ? 'bg-gray-800 text-white'
            : 'text-gray-300 hover:bg-gray-800 hover:text-white'
        }`
      }
      onClick={() => setIsOpen(false)}
    >
      <Icon className="h-5 w-5" />
      <span>{children}</span>
    </NavLink>
  );

  return (
    <>
      {/* Mobile menu button */}
      <button
        className="md:hidden fixed top-4 left-4 z-20 p-2 rounded-md bg-gray-900 text-white"
        onClick={toggleSidebar}
      >
        {isOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
      </button>

      {/* Overlay */}
      {isOpen && (
        <div className="md:hidden fixed inset-0 bg-black bg-opacity-50 z-10" onClick={toggleSidebar} />
      )}

      {/* Sidebar */}
      <aside
        className={`
          fixed top-0 left-0 z-20 h-full w-64 bg-gray-900 p-6 space-y-6 transition-transform duration-300 ease-in-out transform
          ${isOpen ? 'translate-x-0' : '-translate-x-full'}
          md:relative md:translate-x-0
        `}
      >
        <nav className="space-y-4">
          <NavItem to="/profile" icon={User}>Profile</NavItem>
          <NavItem to="/tournaments" icon={Trophy}>Tournaments</NavItem>
          <NavItem to="/clubs" icon={Users}>Club</NavItem>
        </nav>
      </aside>
    </>
  );
}
