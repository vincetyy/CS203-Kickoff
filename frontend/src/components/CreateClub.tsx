import { useState } from 'react';
import { toast } from 'react-hot-toast';
import { Button } from './ui/button';
import { Input } from './ui/input';
import axios from 'axios';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "./ui/dialog"; 
import { Club } from '../types/club';  
import { useNavigate } from 'react-router-dom';

interface CreateClubProps {
  isCreateDialogOpen: boolean;
  setIsCreateDialogOpen: (open: boolean) => void;
  handleClubCreated: (newClub: Club) => void;
}

const CreateClub: React.FC<CreateClubProps> = ({ isCreateDialogOpen, setIsCreateDialogOpen, handleClubCreated }) => {
  const [clubName, setClubName] = useState('');
  const [elo, setElo] = useState<number>(1500);
  const [ratingDeviation, setRatingDeviation] = useState<number>(200);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const fetchPlayersForClub = async (clubId: number) => {
    try {
      const response = await axios.get(`http://localhost:8082/clubs/${clubId}/players`);
      return response.data;  
    } catch (err: any) {
      console.error('Error fetching players:', err);
      toast.error('Failed to fetch players');
      return [];
    }
  };

  const handleCreateClub = async () => {
    if (!clubName) {
      toast.error('Club name is required!');
      return;
    }

    const clubData = {
      club: {
        name: clubName,
        elo,
        ratingDeviation,
      }
    };

    try {
      setLoading(true);

      const createClubResponse = await axios.post('http://localhost:8082/clubs/create-club', clubData, {
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (createClubResponse.status === 201) {
        const newClub = createClubResponse.data;  
        toast.success('Club created successfully!');
        const players = await fetchPlayersForClub(newClub.id);
        const completeClub: Club = {
          ...newClub,
          players: players || [], 
        };
        handleClubCreated(completeClub);        

        // Reset the form and close the dialog
        setIsCreateDialogOpen(false);       
        setClubName('');
        setElo(1500);
        setRatingDeviation(200);
        navigate(`/clubs/${newClub.id}`);
      }
    } catch (err: any) {
      console.error('Error creating club:', err);
      setError(err.response?.data?.message || 'Failed to create the club');
      toast.error(err.response?.data?.message || 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent className="sm:max-w-[600px] lg:max-w-[800px]">
          <DialogHeader>
            <DialogTitle>Create New Club</DialogTitle>
          </DialogHeader>

          {/* Club creation form */}
          <div className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label htmlFor="clubName" className="form-label">Club Name</label>
                <Input
                  id="clubName"
                  name="clubName"
                  value={clubName}
                  onChange={(e) => setClubName(e.target.value)}
                  className="form-input"
                  required
                />
              </div>
              <div>
                <label htmlFor="elo" className="form-label">ELO Rating</label>
                <Input
                  id="elo"
                  name="elo"
                  type="number"
                  value={elo}
                  onChange={(e) => setElo(Number(e.target.value))}
                  className="form-input"
                  required
                />
              </div>
              <div>
                <label htmlFor="ratingDeviation" className="form-label">Rating Deviation</label>
                <Input
                  id="ratingDeviation"
                  name="ratingDeviation"
                  type="number"
                  value={ratingDeviation}
                  onChange={(e) => setRatingDeviation(Number(e.target.value))}
                  className="form-input"
                  required
                />
              </div>
            </div>
            <div className="flex justify-end space-x-2 mt-6">
              <Button 
                type="button" 
                onClick={() => setIsCreateDialogOpen(false)} 
                className="bg-gray-600 hover:bg-gray-700"
              >
                Cancel
              </Button>
              <Button 
                type="button" 
                onClick={handleCreateClub} 
                className="bg-blue-600 hover:bg-blue-700"
                disabled={loading}
              >
                {loading ? 'Creating...' : 'Create Club'}
              </Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
};

export default CreateClub;
