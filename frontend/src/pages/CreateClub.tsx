import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Toaster, toast } from 'react-hot-toast';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import axios from 'axios'; 

interface Club {
  name: string;
  elo: number;
  ratingDeviation: number;
  creatorId: number;
}

export default function CreateClub() {
  const [clubName, setClubName] = useState('');
  const [elo, setElo] = useState<number>(1500);
  const [ratingDeviation, setRatingDeviation] = useState<number>(200);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  // Temporarily hardcoding creatorId for testing
  const creatorId = 1; 

  const handleCreateClub = async () => {
    if (!clubName) {
      toast.error('Club name is required!');
      return;
    }

    const clubData = {
      club: {
        name: clubName,
        elo,
        ratingDeviation
      },
      creatorId
    };
    

    try {
      setLoading(true);
      console.log(clubData);  // Log the payload for debugging
      const handleCreateClub = async () => {
  if (!clubName) {
    toast.error('Club name is required!');
    return;
  }

  const clubData = {
    club: {
      name: clubName,
      elo,
      ratingDeviation
    },
    creatorId 
  };

  try {
    setLoading(true);
    console.log(clubData);  // Log the payload for debugging

    const response = await axios.post('http://localhost:8080/clubs/create-club', clubData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
    });

    // Handle success response
    if (response.status === 201) {
      toast.success('Club created successfully!');
      navigate('/club'); // Navigate to club details page
    }
  } catch (err) {
    console.error('Error creating club:', err);
    setError(err.response?.data?.message || 'Failed to create the club');
    toast.error(err.response?.data?.message || 'An error occurred');
  } finally {
    setLoading(false);
  }
};

      const response = await axios.post('http://localhost:8080/clubs/create-club', clubData, {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
      });

      // Handle success response
      if (response.status === 201) {
        toast.success('Club created successfully!');
        navigate('/club'); // Navigate to club list page
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
    <div className="p-8 bg-gray-900 text-white min-h-screen">
      <Toaster />
      <h1 className="text-3xl font-bold mb-6">Create a New Club</h1>

      <div className="bg-gray-800 p-6 rounded-lg">
        <div className="mb-4">
          <label className="block text-sm font-semibold mb-2">Club Name</label>
          <Input
            type="text"
            name="clubName"
            value={clubName}
            onChange={(e) => setClubName(e.target.value)}
            required
          />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-semibold mb-2">ELO Rating</label>
          <Input
            type="number"
            name="elo"
            value={elo}
            onChange={(e) => setElo(Number(e.target.value))}
          />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-semibold mb-2">Rating Deviation</label>
          <Input
            type="number"
            name="ratingDeviation"
            value={ratingDeviation}
            onChange={(e) => setRatingDeviation(Number(e.target.value))}
          />
        </div>

        {error && <p className="text-red-500 mb-4">{error}</p>}

        <Button
          onClick={handleCreateClub}
          disabled={loading}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full"
        >
          {loading ? 'Creating...' : 'Create Club'}
        </Button>
      </div>
    </div>
  );
}
