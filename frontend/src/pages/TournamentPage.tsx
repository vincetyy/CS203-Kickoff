import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-hot-toast';
import { AppDispatch } from '../store';
import { Button } from "../components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog";

import { Tournament, TournamentUpdate } from '../types/tournament';
import { useDispatch, useSelector } from 'react-redux';
import { removeClubFromTournamentAsync, updateTournamentAsync } from '../store/tournamentSlice';
import { PlayerAvailabilityDTO } from '../types/playerAvailability'; 
import ShowAvailability from '../components/ShowAvailability';
import AvailabilityButton from '../components/AvailabilityButton'; 
import { fetchTournamentById, getPlayerAvailability, updatePlayerAvailability } from '../services/tournamentService';
import { getClubProfileById } from '../services/clubService' 
import { fetchUserClubAsync, selectUserClub, selectUserId,  } from '../store/userSlice'

import UpdateTournament from '../components/UpdateTournament';
import { Club, ClubProfile } from '../types/club';
import { fetchPlayerProfileById } from '../services/userService';
import { ArrowLeft } from 'lucide-react';



const TournamentPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();

  useEffect(() => {
    dispatch(fetchUserClubAsync());
  }, [dispatch])

  const [isRemoveDialogOpen, setIsRemoveDialogOpen] = useState(false);
  const [clubToRemove, setClubToRemove] = useState<Club | null>(null);

  // State for Update Tournament Dialog
  const [isUpdateDialogOpen, setIsUpdateDialogOpen] = useState(false);
  const [initialUpdateData, setInitialUpdateData] = useState<TournamentUpdate | null>(null);
  const [availabilities, setAvailabilities] = useState<PlayerAvailabilityDTO[]>([]);
  const [isAvailabilityDialogOpen, setIsAvailabilityDialogOpen] = useState(false);
  const [joinedClubsProfiles, setJoinedClubsProfiles] = useState<ClubProfile[] | null>(null);
  const [hostUsername, setHostUsername] = useState('');

  const { id } = useParams<{ id: string }>();
  const tournamentId = id ? parseInt(id, 10) : null;
  const userId = useSelector(selectUserId);
  const userClub: Club | null = useSelector(selectUserClub);

  const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null);
  const [status, setStatus] = useState<'idle' | 'loading' | 'succeeded' | 'failed'>('idle');
  const [error, setError] = useState<string | null>(null); 

  const isHost = selectedTournament ? selectedTournament.host === userId : false;

  let isCaptain = false;
  
  if (userClub) {
    isCaptain = userClub?.captainId === userId;
  }

  const tournamentFormatMap: { [key: string]: string } = {
    FIVE_SIDE: 'Five-a-side',
    SEVEN_SIDE: 'Seven-a-side'
  };

  const knockoutFormatMap: { [key: string]: string } = {
    SINGLE_ELIM: 'Single Elimination',
    DOUBLE_ELIM: 'Double Elimination'
  };


  const handleOpenRemoveDialog = (club: Club) => {
    setClubToRemove(club);
    setIsRemoveDialogOpen(true);
  };

  const handleConfirmRemove = async () => {
    if (clubToRemove && selectedTournament && selectedTournament.id) {
      await dispatch(removeClubFromTournamentAsync({ 
        tournamentId: selectedTournament.id, 
        clubId: clubToRemove.id 
      })).unwrap();
      const updatedTournamentData = await fetchTournamentById(selectedTournament.id);
      setSelectedTournament(updatedTournamentData);
      setJoinedClubsProfiles(prevProfiles => prevProfiles ? 
        prevProfiles.filter(club => club.id !== clubToRemove.id) 
        : prevProfiles
      );
      toast.success('Club removed successfully');
    }
    setIsRemoveDialogOpen(false);
  };

  useEffect(() => {
    if (tournamentId === null || isNaN(tournamentId)) {
      setError('Invalid tournament ID.');
      setStatus('failed');
      return;
    }

    const fetchData = async () => {
      try {
        setStatus('loading');
        const tournament = await fetchTournamentById(tournamentId);
        setSelectedTournament(tournament);
        if (tournament.host) {
          const hostId = tournament.host;
          const hostProfile = await fetchPlayerProfileById(hostId.toString());
          setHostUsername(hostProfile.user.username);
        }
        
        

        if (tournament.joinedClubsIds) {
          const clubProfilesPromises = tournament.joinedClubsIds.map((id) => getClubProfileById(id));
  
          // Wait for all promises to resolve
          const clubProfiles = await Promise.all(clubProfilesPromises);
          
          setJoinedClubsProfiles(clubProfiles);
        }
        

        const availabilities = await getPlayerAvailability(tournamentId);
        setAvailabilities(availabilities);
        setStatus('succeeded');
      } catch (err) {
        console.error('Error fetching tournament data:', err);
        toast.error('Failed to load tournament data.');
        setStatus('failed');
        setError('Failed to load tournament data.');
      }
    };

    fetchData();
  }, [tournamentId]);


  const handleAvailabilityUpdate = async (availability: boolean) => {
    if (tournamentId === null || isNaN(tournamentId)) {
      toast.error('Invalid tournament ID.');
      return;
    }
  
    if (!userClub) {
      toast.error("You must be part of a club to mark availability.");
      return;
    }

    try {
      const payload = {
        tournamentId: tournamentId,
        playerId: userId,
        clubId: userClub.id,  // Use the fetched clubId
        available: availability  
      };
  
      console.log('Updating availability: ', payload);
      await updatePlayerAvailability(payload);
  
      // Refetch or update availabilities after the change
      const updatedAvailabilities = await getPlayerAvailability(tournamentId);
      setAvailabilities(updatedAvailabilities);
  
      toast.success(`You have marked yourself as ${availability ? 'available' : 'not available'}.`);
    } catch (err) {
      console.error('Error updating availability:', err);
      toast.error('Failed to update your availability.');
    }
  };  
  
  
  const formatDate = (dateString: string) => {
    const options: Intl.DateTimeFormatOptions = { 
      year: 'numeric', month: 'long', day: 'numeric', 
      hour: '2-digit', minute: '2-digit' 
    };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  const handleUpdateClick = () => {
    if (selectedTournament) {
      const initialData: TournamentUpdate = {
        name: selectedTournament.name,
        startDateTime: selectedTournament.startDateTime,
        endDateTime: selectedTournament.endDateTime,
        location: selectedTournament.location || null,
        prizePool: selectedTournament.prizePool || [],
        minRank: selectedTournament.minRank || 0,
        maxRank: selectedTournament.maxRank || 0,
      };
      setInitialUpdateData(initialData);
      setIsUpdateDialogOpen(true);
    }
  };

  const handleUpdateTournament = async (data: TournamentUpdate) => {
    if (selectedTournament === null || tournamentId === null) {
      throw new Error('Invalid tournament data.');
    }
    if (!selectedTournament.id) return;
    await dispatch(updateTournamentAsync({ 
      tournamentId: selectedTournament.id,
      tournamentData: data
    })).unwrap();

    const updatedTournamentData = await fetchTournamentById(tournamentId);
    setSelectedTournament(updatedTournamentData);
  };


  if (status === 'loading') return <div className="text-center mt-10">Loading tournament details...</div>;
  if (status === 'failed') return <div className="text-center mt-10 text-red-500">Error: {error}</div>;
  if (!selectedTournament) return <div className="text-center mt-10">No tournament found.</div>;

  return (
    <>
      <div className='pb-2'>
        <Button variant="ghost" size="icon" onClick={() => navigate(-1)}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
      </div>

      {/* Tournament Details Banner */}
      <div className="bg-green-600 rounded-lg p-4 lg:p-6 mb-6 flex items-center space-x-4">
        <div className="bg-white rounded-full p-2 lg:p-3">
          <svg className="h-6 w-6 lg:h-8 lg:w-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
        </div>
        <div>
          <h2 className="text-xl lg:text-2xl font-bold">{selectedTournament.name}</h2>
          <p className="text-sm lg:text-base">{selectedTournament.location?.name || 'No location'}</p>
        </div>
      </div>

      {/* Tournament Information */}
      <div className="bg-gray-800 rounded-lg p-6 mb-6">
        <h3 className="text-2xl font-semibold mb-4">Tournament Details</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p><strong>Host:</strong> {hostUsername}</p>
            <p><strong>Start Date & Time:</strong> {formatDate(selectedTournament.startDateTime)}</p>
            <p><strong>End Date & Time:</strong> {formatDate(selectedTournament.endDateTime)}</p>
            <p><strong>Location:</strong> {selectedTournament.location?.name || 'No location specified'}</p>
            <p><strong>Max Teams:</strong> {selectedTournament.maxTeams}</p>
          </div>
          <div>
            <p><strong>Tournament Format:</strong> {tournamentFormatMap[selectedTournament.tournamentFormat]}</p>
            <p><strong>Knockout Format:</strong> {knockoutFormatMap[selectedTournament.knockoutFormat]}</p>
            <p><strong>Prize Pool:</strong> {selectedTournament.prizePool && selectedTournament.prizePool.length > 0 ? `$${selectedTournament.prizePool.join(', ')}` : 'N/A'}</p>
            <p><strong>Rank Range:</strong> {selectedTournament.minRank !== null && selectedTournament.maxRank !== null ? `${selectedTournament.minRank} - ${selectedTournament.maxRank}` : 'N/A'}</p>
          </div>
        </div>
      </div>

      {/* Joined Clubs */}
      <div className="bg-gray-800 rounded-lg p-6 mb-6">
        <h3 className="text-2xl font-semibold mb-4">Joined Clubs</h3>
        {joinedClubsProfiles && joinedClubsProfiles.length === 0 ? (
          <p>No clubs have joined this tournament yet.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {joinedClubsProfiles?.map((club: ClubProfile) => {
              const isUserClub = club.id === userClub?.id;
              return (
                <div onClick={ () => navigate(`/clubs/${club.id}`) } key={club.id} className="bg-gray-700 rounded-lg p-4 flex items-center justify-between space-x-4">
                  <div className="flex items-center space-x-4">
                    <img 
                      src={`https://picsum.photos/seed/${club.id}/100/100`} 
                      alt={club.name} 
                      className="w-16 h-16 rounded-full object-cover" 
                    />
                    <div>
                      <h4 className="text-lg font-bold">{club.name}</h4>
                    </div>
                  </div>
                  {(isHost || (isCaptain && isUserClub)) && (
                    <button 
                      onClick={() => handleOpenRemoveDialog(club)} 
                      className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
                    >
                      {isUserClub ? 'Leave' : 'Remove'}
                    </button>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </div>
        
      {/* Show Availability */}
      {
        userClub &&
        <ShowAvailability 
          availabilities={availabilities} 
          currentUserId={userId} 
          currentUserClubId={userClub.id !== null ? userClub.id : undefined} 
        />
      }
      

      {/* Remove Confirmation Dialog */}
      <Dialog open={isRemoveDialogOpen} onOpenChange={setIsRemoveDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Remove {clubToRemove?.name}</DialogTitle>
          </DialogHeader>
          <div className="mt-4">
            <p> {clubToRemove?.id === userClub?.id ? `Are you sure you want to leave this tournament?` : `Are you sure you want to remove ${clubToRemove?.name} from this tournament?`}</p>
          </div>
          <div className="flex flex-col sm:flex-row justify-between mt-4 space-y-2 sm:space-y-0 sm:space-x-2">
            <button 
              className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 w-full" 
              onClick={() => setIsRemoveDialogOpen(false)}
            >
              Cancel
            </button>
            <button 
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 w-full"
              onClick={handleConfirmRemove}
            >
              Confirm
            </button>
          </div>
        </DialogContent>
      </Dialog>

      {/* Update Tournament Dialog */}
      <UpdateTournament
        isOpen={isUpdateDialogOpen}
        onClose={() => setIsUpdateDialogOpen(false)}
        initialData={initialUpdateData!}
        onUpdate={handleUpdateTournament}
      />

      {/* Availability Button Dialog */}
      <Dialog open={isAvailabilityDialogOpen} onOpenChange={setIsAvailabilityDialogOpen}>
        <DialogContent>
          <div>
          <AvailabilityButton
            onAvailabilitySelect={(availability: boolean) => {
              handleAvailabilityUpdate(availability);  
              setIsAvailabilityDialogOpen(false);  
            }}
          />
          </div>
        </DialogContent>
      </Dialog>

      {/* Back, Update, and Indicate Availability Buttons */}
      <div className="flex space-x-3 mb-4">
        {isHost && (
          <Button
            type="button"
            onClick={handleUpdateClick}
            className="bg-blue-600 hover:bg-blue-700"
          >
            Update Tournament
          </Button>
        )}
        {
          userClub &&
          <Button
            onClick={() => setIsAvailabilityDialogOpen(true)}
            className="bg-blue-600 hover:bg-blue-700"
          >
            Indicate Availability
          </Button>
        }
      </div>
    </>
  );
};

export default TournamentPage;
