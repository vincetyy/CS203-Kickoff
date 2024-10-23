import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchClubsAsync, applyToClubAsync } from '../store/clubSlice';
import { AppDispatch, RootState } from '../store';
import { Search } from 'lucide-react';
import { Input } from '../components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../components/ui/select';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '../components/ui/dialog';
import { Button } from '../components/ui/button';
import ClubCard from '../components/ClubCard';
import ClubInfoModal from '../components/ClubInfoModal';
import {  toast } from 'react-hot-toast';
import { Club } from '../types/club';
import CreateClub from '../components/CreateClub';
import { fetchUserClubAsync, selectUserId } from '../store/userSlice';

enum PlayerPosition {
  POSITION_FORWARD = 'POSITION_FORWARD',
  POSITION_MIDFIELDER = 'POSITION_MIDFIELDER',
  POSITION_DEFENDER = 'POSITION_DEFENDER',
  POSITION_GOALKEEPER = 'POSITION_GOALKEEPER',
}

export default function ClubPage() {
  const dispatch = useDispatch<AppDispatch>();
  const userId = useSelector(selectUserId);
  const { clubs, status, error } = useSelector(
    (state: RootState) => state.clubs
  );
  const { userClub } = useSelector((state: RootState) => state.user);
  const [filteredClubs, setFilteredClubs] = useState<Club[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedClub, setSelectedClub] = useState<Club | null>(null);
  const [selectedPosition, setSelectedPosition] =
    useState<PlayerPosition | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isInfoDialogOpen, setIsInfoDialogOpen] = useState(false);
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);

  useEffect(() => {
    dispatch(fetchClubsAsync());
    dispatch(fetchUserClubAsync());
  }, [dispatch]);

  useEffect(() => {
    const results = clubs.filter(
      (club) =>
        club.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        (club.clubDescription &&
          club.clubDescription.toLowerCase().includes(searchTerm.toLowerCase()))
    );
    setFilteredClubs(results);
  }, [searchTerm, clubs]);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const handleCreateClub = (newClub: Club) => {
    setFilteredClubs(prevClubs => [...prevClubs, newClub]); // Add the new club to the list
  };

  const formatPosition = (position: string) => {
    return (
      position.replace('POSITION_', '').charAt(0) +
      position.replace('POSITION_', '').slice(1).toLowerCase()
    );
  };

  const handleCardClick = (club: Club) => {
    setSelectedClub(club);
    setIsInfoDialogOpen(true);
  };

  const handleApplyClick = () => {
    setIsInfoDialogOpen(false); // Close the club info dialog
    setIsDialogOpen(true); // Open the position selection dialog
  };

  const handleApply = async () => {
    if (!selectedClub || !selectedPosition) return;

    try {
      await dispatch(
        applyToClubAsync({
          clubId: selectedClub.id,
          playerProfileId: 1, // Replace with actual player ID
          desiredPosition: selectedPosition,
        })
      ).unwrap();
      toast.success(
        `Successfully applied to ${selectedClub.name} as ${formatPosition(
          selectedPosition
        )}`,
        {
          duration: 3000,
          position: 'top-center',
        }
      );
      setIsDialogOpen(false);
      setSelectedClub(null);
      setSelectedPosition(null);
    } catch (err) {
      console.error('Error applying to club:', err);
      toast.error(`Error applying to club: ${(err as any).message}`, {
        duration: 4000,
        position: 'top-center',
      });
    }
  };

  const handlePositionChange = (position: string) => {
    setSelectedPosition(position as PlayerPosition);
  };

  const handleCreateClubClick = () => {
    setIsCreateDialogOpen(true);  // Open the CreateClub modal
  };

  if (status === 'loading') return <div>Loading...</div>;
  if (status === 'failed') return <div>Error: {error}</div>;

  return (
    <>
      {/* Banner */}
      <div className="bg-blue-600 rounded-lg p-4 lg:p-6 mb-6 flex items-center space-x-4">
        <div className="bg-yellow-400 rounded-full p-2 lg:p-3">
          <svg
            className="h-6 w-6 lg:h-8 lg:w-8 text-blue-600"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M12 6v6m0 0v6m0-6h6m-6 0H6"
            />
          </svg>
        </div>
        <div>
          <h2 className="text-xl lg:text-2xl font-bold">
            {filteredClubs.length} soccer clubs
          </h2>
          <p className="text-sm lg:text-base">waiting for you</p>
        </div>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center space-y-2 lg:space-y-0 lg:space-x-4 mb-6">
        <div className="flex flex-col lg:flex-row space-y-2 lg:space-y-0 lg:space-x-4 w-full">
          <div className="relative w-full lg:w-[300px]">
            <Search className="absolute left-2 top-2.5 h-4 w-4 text-gray-500" />
            <Input
              type="search"
              placeholder="Search clubs"
              className="pl-8 bg-gray-800 border-gray-700 w-full h-10"
              value={searchTerm}
              onChange={handleSearch}
            />
          </div>
          {/* Additional filters can be added here */}
        </div>

        { userId && !userClub &&
          <Button onClick={handleCreateClubClick} className="bg-blue-600 hover:bg-blue-700 w-full lg:w-auto">
            Create Club
          </Button>
        }
      </div>

      {/* Club Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 lg:gap-6">
        {filteredClubs.map((club) => (
          <ClubCard
            key={club.id}
            id={club.id}
            name={club.name}
            description={
              club.clubDescription 
                ? `${club.clubDescription}`
                : 'This club has no description yet.'
            }
            ratings={`ELO: ${club.elo.toFixed(0)}, RD: ${club.ratingDeviation.toFixed(0)}`}
            image={`https://picsum.photos/seed/${club.id}/400/300`}
            applied={false}
            onClick={() => handleCardClick(club)}
          />
        ))}
      </div>

      {/* Club Info Dialog */}
      <Dialog open={isInfoDialogOpen} onOpenChange={setIsInfoDialogOpen}>
        <DialogContent className="sm:max-w-[600px]">
          <DialogHeader>
            <DialogTitle>{selectedClub?.name}</DialogTitle>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            {selectedClub && (
              <ClubInfoModal
                clubId={selectedClub.id}
                onApplyClick={handleApplyClick}
              />
            )}
          </div>
          <Button onClick={() => setIsInfoDialogOpen(false)}>Close</Button>
        </DialogContent>
      </Dialog>

      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent className="sm:max-w-[600px] lg:max-w-[800px]">
          <DialogHeader>
            <DialogTitle>Create New Club</DialogTitle>
          </DialogHeader>
          <CreateClub 
            isCreateDialogOpen={isCreateDialogOpen}
            setIsCreateDialogOpen={setIsCreateDialogOpen}
            handleClubCreated={handleCreateClub}
          />
        </DialogContent>
      </Dialog>

      {/* Position Selection Dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Apply to {selectedClub?.name}</DialogTitle>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="flex flex-col justify-between">
              <Select onValueChange={handlePositionChange}>
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
            <Button
              className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 w-full"
              onClick={() => setIsDialogOpen(false)}
            >
              Close
            </Button>
            <Button
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 w-full"
              onClick={handleApply}
              disabled={!selectedPosition}
            >
              Apply
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
