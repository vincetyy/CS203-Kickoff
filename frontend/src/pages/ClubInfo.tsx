import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; // Adjust if using Next.js
import axios from 'axios';
import { Toaster, toast } from 'react-hot-toast';
import { Button } from '../components/ui/button';
import { ClubProfile } from '../types/club'; // Adjust the import path as necessary

const ClubInfo: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [club, setClub] = useState<ClubProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchClub = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/clubs/${id}`);
        setClub(response.data);
      } catch (err: any) {
        console.error('Error fetching club info:', err);
        setError('Failed to fetch club information.');
      } finally {
        setLoading(false);
      }
    };

    fetchClub();
  }, [id]);

  const handleApply = async () => {
    try {
      // Replace `userId` with the actual player ID from your auth context
      const userId = 1; // Placeholder
      await axios.post(`http://localhost:8080/clubs/${id}/apply`, {
        playerId: userId,
      });
      toast.success('Application sent successfully!');
    } catch (err: any) {
      console.error('Error applying to club:', err);
      toast.error('Failed to apply to club.');
    }
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  if (error || !club) {
    return <div className="flex justify-center items-center h-screen">{error || 'Club not found.'}</div>;
  }

  return (
    <div className="container mx-auto p-6">
      <Toaster />
      <h1 className="text-3xl font-bold mb-4">{club.name}</h1>
      <p className="text-lg mb-2">{club.description}</p>
      <p className="mb-2">
        <strong>Captain:</strong> {club.captainName}
      </p>
      <p className="mb-4">
        <strong>ELO:</strong> {club.elo.toFixed(2)}
      </p>

      {/* Players List */}
      <details className="mb-4">
        <summary className="cursor-pointer font-semibold">Players in the Club</summary>
        <ul className="list-disc list-inside mt-2">
          {club.playerNames.map((playerName, index) => (
            <li key={index}>{playerName}</li>
          ))}
        </ul>
      </details>

      {/* Apply Button */}
      <Button onClick={handleApply}>Apply to Join</Button>
    </div>
  );
};

export default ClubInfo;