import { Menu, Bell, MessageSquare } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Button } from './ui/button';
import { Avatar, AvatarFallback, AvatarImage } from './ui/avatar';
import { Sheet, SheetContent, SheetTrigger } from './ui/sheet';
import Sidebar from './Sidebar';  
import { Toaster } from 'react-hot-toast';
import { useSelector } from 'react-redux';
import { selectUsername } from '../store/userSlice';
import { useNavigate } from 'react-router-dom';
import { Club } from '../types/club'; 
import { selectUserClub } from '../store/userSlice';


interface HeaderProps {
  isSidebarOpen: boolean;
  newApplications: boolean;
}

export default function Header({ isSidebarOpen, setIsSidebarOpen, newApplications }: HeaderProps) {
  const username = useSelector(selectUsername);
  const navigate = useNavigate();
  const userClub: Club | null = useSelector(selectUserClub);
  const clubId = userClub?.id;

  const handleBellClick = () => {
    console.log('clubId:', clubId); 
    if (clubId) {
      navigate(`/clubs/${clubId}/applications`);
    } else {
      console.error('No club selected');
    }
  };

  const avatarFallbackText = username ? username.slice(0, 2).toUpperCase() : "";

  return (
    <header className="flex justify-between items-center p-4 bg-gray-900">
      <Toaster />
      <div className="flex items-center">
        <Sheet open={isSidebarOpen} onOpenChange={setIsSidebarOpen}>
          <SheetTrigger asChild>
            <Button variant="ghost" size="icon" className="lg:hidden">
              <Menu className="h-6 w-6" />
            </Button>
          </SheetTrigger>
          <SheetContent side="left" className="w-64 p-0">
            <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} /> {/* Pass correct props */}
          </SheetContent>
        </Sheet>
        <Link to="/" className="text-2xl font-bold ml-2 text-white hover:text-gray-300 transition-colors">
          KICKOFF
        </Link>
      </div>
      <div className="flex items-center space-x-4">
        <Button
          variant="ghost"
          className="relative"
          onClick={handleBellClick}
        >
          <Bell className="h-6 w-6 text-blue-500" /> {/* Make the bell icon blue */}
          {newApplications && (
            <span className="absolute top-0 right-0 block h-3 w-3 rounded-full bg-red-500 ring-2 ring-white" />
          )}
        </Button>
        <Button variant="ghost" size="icon">
          <MessageSquare className="h-5 w-5" />
        </Button>
        <Avatar>
          <AvatarFallback>{avatarFallbackText}</AvatarFallback>
        </Avatar>
      </div>
    </header>
  );
}
