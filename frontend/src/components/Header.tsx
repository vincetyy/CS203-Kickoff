import { Menu, Bell, MessageSquare } from 'lucide-react'
import { Link } from 'react-router-dom'
import { Button } from "./ui/button"
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar"
import { Sheet, SheetContent, SheetTrigger } from "./ui/sheet"
import Sidebar from './Sidebar'

export default function Header({ isSidebarOpen, setIsSidebarOpen }: { isSidebarOpen: boolean; setIsSidebarOpen: (isOpen: boolean) => void }) {
  return (
    <header className="flex justify-between items-center p-4 bg-gray-900">
      <div className="flex items-center">
        <Sheet open={isSidebarOpen} onOpenChange={setIsSidebarOpen}>
          <SheetTrigger asChild>
            <Button variant="ghost" size="icon" className="lg:hidden">
              <Menu className="h-6 w-6" />
            </Button>
          </SheetTrigger>
          <SheetContent side="left" className="w-64 p-0">
            <Sidebar />
          </SheetContent>
        </Sheet>
        <Link to="/" className="text-2xl font-bold ml-2 text-white hover:text-gray-300 transition-colors">
          KICKOFF
        </Link>
      </div>
      <div className="flex items-center space-x-4">
        <Button variant="ghost" size="icon">
          <Bell className="h-5 w-5" />
        </Button>
        <Button variant="ghost" size="icon">
          <MessageSquare className="h-5 w-5" />
        </Button>
        <Avatar>
          <AvatarFallback>CN</AvatarFallback>
        </Avatar>
      </div>
    </header>
  )
}