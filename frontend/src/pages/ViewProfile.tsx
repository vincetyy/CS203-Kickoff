import { useState, useEffect } from 'react';
import { PlayerPosition, PlayerProfile } from '../types/profile';
import { fetchPlayerProfileById } from '../services/userService';
import { getClubByPlayerId } from '../services/clubService';
import { Club } from '../types/club';
import { useNavigate, useParams } from 'react-router-dom';
import { Button } from '../components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { ArrowLeft, Trophy, User } from 'lucide-react';
import { Badge } from '../components/ui/badge';

export default function ViewProfile() {

  const navigate = useNavigate();

  const { id } = useParams<{ id: string }>();
  
  const [playerProfile, setPlayerProfile] = useState<PlayerProfile | null>(null);
  const [club, setClub] = useState<Club | null>(null);
  const [preferredPositions, setPreferredPositions] = useState<PlayerPosition[]>([]);
  const [profileDescription, setProfileDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Fetch player profile when logged in
  useEffect(() => {
    if (!id) {
      setError('User not found');
      setLoading(false);
      return;
    }

    const fetchPlayerProfile = async () => {
      try {
        const response = await fetchPlayerProfileById(id);
        setPlayerProfile(response);
        setPreferredPositions(response.preferredPositions || []);
        setProfileDescription(response.profileDescription || '');
        setLoading(false);
      } catch (err) {
        console.error('Error fetching player profile:', err);
        setLoading(false);
      }

      try {
        const clubResponse = await getClubByPlayerId(parseInt(id));
        setClub(clubResponse);
      } catch (err) {
        console.error('Error fetching club:', err);
      }
    };

    fetchPlayerProfile();
  }, [id]);


  const formatPosition = (position: string) => {
    return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
  };

  // Render profile page if user is logged in
  if (loading) return <div>Loading...</div>;

  if (error || !playerProfile) return <div>Error: {error || 'Profile not found'}</div>;

  return (
    <div className="container mx-auto p-6 max-w-4xl">
      <Card className="mb-6">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <Button variant="ghost" size="icon" onClick={() => navigate(-1)}>
            <ArrowLeft className="h-4 w-4" />
          </Button>
        </CardHeader>
        <CardContent>
          <div className="flex flex-col md:flex-row items-center md:items-start gap-6">
            <img
              src={`https://picsum.photos/seed/${playerProfile.id + 2000}/200/200`}
              alt={`${playerProfile.user.username}'s profile`}
              className="w-32 h-32 rounded-full object-cover"
            />
            <div className="text-center md:text-left">
              <h1 className="text-3xl font-bold">{playerProfile.user.username}</h1>
              <p className="text-muted-foreground">Player ID: {playerProfile.id}</p>
              <div className="mt-4 flex flex-wrap gap-2 justify-center md:justify-start">
                {preferredPositions.map((position) => (
                  <Badge key={position} >
                    {formatPosition(position)}
                  </Badge>
                ))}
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
          <CardContent>
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
              <p className="text-muted-foreground">You are not currently associated with a club.</p>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-xl font-semibold flex items-center gap-2">
              <User className="h-5 w-5" />
              Profile Description
            </CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-muted-foreground">{profileDescription || 'No description provided.'}</p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
