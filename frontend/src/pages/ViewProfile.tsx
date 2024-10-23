import React, { useState, useEffect } from 'react';
import { Input } from '../components/ui/input';
import { PlayerPosition, PlayerProfile } from '../types/profile';
import { fetchPlayerProfileById } from '../services/userService';
import { getClubByPlayerId } from '../services/clubService';
import { Club } from '../types/club';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button } from '../components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { ArrowLeft, Calendar, Pencil, Trophy, User } from 'lucide-react';
import { getTournamentsHosted } from '../services/tournamentService';
import { Tournament } from '../types/tournament';
import TournamentCard from '../components/TournamentCard';
import { selectUserId } from '../store/userSlice';
import { useSelector } from 'react-redux';

export default function ViewProfile() {

  const navigate = useNavigate();
  const userId = useSelector(selectUserId);

  const { id } = useParams<{ id: string }>();

  const playerId = id ? id : userId;
  
  const [playerProfile, setPlayerProfile] = useState<PlayerProfile | null>(null);
  const [club, setClub] = useState<Club | null>(null);
  const [preferredPositions, setPreferredPositions] = useState<PlayerPosition[]>([]);
  const [profileDescription, setProfileDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [tournamentsHosted, setTournamentsHosted] = useState<Tournament[] | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Fetch player profile when logged in
  useEffect(() => {
    if (!playerId) {
      setError('User not found');
      setLoading(false);
      return;
    }

    const fetchPlayerProfile = async () => {
      try {
        const response = await fetchPlayerProfileById(playerId);
        setPlayerProfile(response);
        setPreferredPositions(response.preferredPositions || []);
        setProfileDescription(response.profileDescription || '');

        const hostResponse = await getTournamentsHosted(parseInt(playerId));
        setTournamentsHosted(hostResponse);

        console.log(hostResponse);

        setLoading(false);
      } catch (err) {
        console.error('Error fetching player profile:', err);
        setLoading(false);
      }

      try {
        const clubResponse = await getClubByPlayerId(parseInt(playerId));
        setClub(clubResponse);
      } catch (err) {
        console.error('Error fetching club:', err);
      }
    };

    fetchPlayerProfile();
  }, [playerId]);


  const formatPosition = (position: string) => {
    return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
  };

  // Render profile page if user is logged in
  if (loading) return <div>Loading...</div>;

  if (error || !playerProfile) return <div>Error: {error || 'Profile not found'}</div>;

  return (
    <div className="container mx-auto p-6 max-w-4xl">
      { id && 
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <Button variant="ghost" size="icon" onClick={() => navigate(-1)}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
      </CardHeader>
      }
      <Card className="mb-6">
        <CardContent>
          <div className="flex flex-col md:flex-row items-center md:items-start gap-6">
            <img
              src={`https://picsum.photos/seed/${playerProfile.id + 2000}/200/200`}
              alt={`${playerProfile.user.username}'s profile`}
              className="w-32 h-32 rounded-full object-cover"
            />
            <div className="text-center md:text-left">
              <div className='flex items-center gap-2'> 
                <h1 className="text-3xl font-bold">{playerProfile.user.username}</h1>
                {!id && (
                  <Button
                    variant="ghost"
                    size="icon"
                    onClick={() => navigate('/profile/edit')}
                    className="p-0"
                  >
                    <Pencil className="h-5 w-5 text-muted-foreground" />
                    <span className="sr-only">Edit Profile</span>
                  </Button>
                )}
              </div>
              
              <p className="text-muted-foreground">ID: {playerProfile.id}</p>
              <div className="mt-4 flex flex-wrap gap-2 justify-center md:justify-start">
                <p className="text-muted-foreground">{profileDescription || 'No user description provided.'}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-6 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-xl font-semibold flex items-center gap-2">
              <Trophy className="h-5 w-5" />
              Club Information
            </CardTitle>
          </CardHeader>
          <CardContent onClick={() => navigate(`/clubs/${club?.id}`)}>
            {club ? (
              <div className="flex items-center gap-4">
                <img
                  src={`https://picsum.photos/seed/club-${club.id}/800/200`}
                  alt={`${club.name} logo`}
                  className="w-16 h-16 rounded-full object-cover"
                />
                <div>
                  <p className="font-semibold">{club.name}</p>
                  <p className="text-sm text-muted-foreground">ELO: {club.elo.toFixed(2)}</p>
                </div>
              </div>
            ) : (
              <p className="text-muted-foreground">Not associated with a club.</p>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-xl font-semibold flex items-center gap-2">
              <User className="h-5 w-5" />
              Player Positions
            </CardTitle>
          </CardHeader>
          <CardContent className="grid grid-cols-2 gap-2">
            {preferredPositions.map((position) => (
              <div
                key={position}
                className="bg-primary text-primary-foreground rounded-full py-1 px-3 text-sm font-medium text-center"
              >
                {formatPosition(position)}
              </div>
            ))}
          </CardContent>
        </Card>
      </div>
      {tournamentsHosted && tournamentsHosted.length > 0 && (
        <Card className="mt-6">
          <CardHeader>
            <CardTitle className="text-xl font-semibold flex items-center gap-2">
              <Calendar className="h-5 w-5" />
              Hosted Tournaments
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
              {tournamentsHosted.map((tournament) => (
                <TournamentCard
                  key={tournament.id}
                  id={tournament.id}
                  name={tournament.name}
                  startDate={new Date(tournament.startDateTime).toLocaleDateString()}
                  endDate={new Date(tournament.endDateTime).toLocaleDateString()}
                  format={tournament.tournamentFormat}
                  teams={`${tournament.joinedClubsIds?.length || 0}/${tournament.maxTeams}`}
                  image={`https://picsum.photos/seed/${tournament.id + 1000}/400/300`}
                />
              ))}
            </div>
          </CardContent>
        </Card>
      )}
    </div>
    
  );
}
