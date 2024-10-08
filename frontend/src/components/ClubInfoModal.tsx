// src/components/ClubInfoModal.tsx

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { ClubProfile } from '../types/club';
import { Button } from './ui/button';

interface ClubInfoModalProps {
  clubId: number;
  onApplyClick: () => void;
}

const ClubInfoModal: React.FC<ClubInfoModalProps> = ({
  clubId,
  onApplyClick,
}) => {
  const [club, setClub] = useState<ClubProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchClub = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/clubs/${clubId}`);
        setClub(response.data);
      } catch (err: any) {
        console.error('Error fetching club info:', err);
        setError('Failed to fetch club information.');
      } finally {
        setLoading(false);
      }
    };

    fetchClub();
  }, [clubId]);

  if (loading) {
    return <div className="flex justify-center items-center h-32">Loading...</div>;
  }

  if (error || !club) {
    return (
      <div className="flex justify-center items-center h-32">
        {error || 'Club not found.'}
      </div>
    );
  }

  return (
    <div>
      <img
        src={`https://picsum.photos/seed/club-${club.id}/800/200`}
        alt={`${club.name} banner`}
        className="w-full h-48 object-cover mb-4 rounded"
      />
      <h1 className="text-3xl font-bold mb-4">{club.name}</h1>
      <p className="text-lg mb-4">{club.description || 'No description available.'}</p>
      <div className="flex items-center mb-4">
        <div className="mr-4">
          <strong>Captain:</strong> {club.captainName || 'No captain assigned.'}
        </div>
        <div>
          <strong>ELO:</strong> {club.elo ? club.elo.toFixed(2) : 'N/A'}
        </div>
      </div>

      {/* Players List */}
      <details className="mb-4">
        <summary className="cursor-pointer font-semibold">
          Players in the Club
        </summary>
        <ul className="list-disc list-inside mt-2">
          {club.playerNames.map((playerName: string, index: number) => (
            <li key={index}>{playerName}</li>
          ))}
        </ul>
      </details>

      {/* Apply Button */}
      <Button onClick={onApplyClick}>Apply to Join</Button>
    </div>
  );
};

export default ClubInfoModal;