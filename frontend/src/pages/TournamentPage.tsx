import React, { useEffect, useState } from 'react'
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { Toaster, toast } from 'react-hot-toast';
import { Loader2 } from 'lucide-react';
import { Button } from "../components/ui/button";

const TournamentPage = () => {
  const { id } = useParams();
  const [tournament, setTournament] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const fetchTournament = async () => {
      try {
        const response = await axios.get(`/api/tournaments/${id}`);
        setTournament(response.data);
      } catch (err) {
        console.error('Error fetching tournament:', err);
        setError(true);
        toast.error('Failed to load tournament data.');
      } finally {
        setLoading(false);
      }
    };

    fetchTournament();
  }, [id]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <Loader2 className="animate-spin h-8 w-8 text-blue-600" />
        <span className="ml-2 text-blue-600">Loading...</span>
      </div>
    );
  }

  if (error || !tournament) {
    return (
      <div className="flex justify-center items-center h-screen">
        <p className="text-red-500">Error loading tournament details.</p>
      </div>
    );
  }

  const {
    name,
    over,
    startDateTime,
    endDateTime,
    location,
    maxTeams,
    tournamentFormat,
    knockoutFormat,
    prizePool,
    minRank,
    maxRank,
    joinedClubs,
  } = tournament;

  return (
    <>
      <Toaster />
      <div className="max-w-4xl mx-auto p-6 bg-white shadow-md rounded-lg mt-6">
        {/* Tournament Header */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-800">{name}</h1>
          {over ? (
            <span className="px-4 py-2 bg-red-500 text-white rounded">Tournament Over</span>
          ) : (
            <span className="px-4 py-2 bg-green-500 text-white rounded">Ongoing</span>
          )}
        </div>

        {/* Tournament Details */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h2 className="text-xl font-semibold text-gray-700 mb-2">Schedule</h2>
            <p><span className="font-medium">Start:</span> {new Date(startDateTime).toLocaleString()}</p>
            <p><span className="font-medium">End:</span> {new Date(endDateTime).toLocaleString()}</p>
          </div>

          <div>
            <h2 className="text-xl font-semibold text-gray-700 mb-2">Location</h2>
            <p><span className="font-medium">Name:</span> {location.name}</p>
            <p><span className="font-medium">ID:</span> {location.id}</p>
          </div>

          <div>
            <h2 className="text-xl font-semibold text-gray-700 mb-2">Format</h2>
            <p><span className="font-medium">Tournament:</span> {tournamentFormat}</p>
            <p><span className="font-medium">Knockout:</span> {knockoutFormat}</p>
          </div>

          <div>
            <h2 className="text-xl font-semibold text-gray-700 mb-2">Prize Pool</h2>
            <ul className="list-disc list-inside">
              {prizePool.map((prize, index) => (
                <li key={index}>${prize.toFixed(2)}</li>
              ))}
            </ul>
          </div>

          <div>
            <h2 className="text-xl font-semibold text-gray-700 mb-2">Team Information</h2>
            <p><span className="font-medium">Max Teams:</span> {maxTeams}</p>
            <p><span className="font-medium">Rank Range:</span> {minRank} - {maxRank}</p>
          </div>
        </div>

        {/* Joined Clubs */}
        <div className="mt-8">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Joined Clubs</h2>
          {joinedClubs.length === 0 ? (
            <p className="text-gray-600">No clubs have joined this tournament yet.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {joinedClubs.map((club) => (
                <div key={club.id} className="flex items-center p-4 bg-gray-100 rounded-lg shadow">
                  <div className="bg-blue-500 text-white rounded-full h-12 w-12 flex items-center justify-center mr-4">
                    {club.name.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <h3 className="text-lg font-semibold">{club.name}</h3>
                    <p>ID: {club.id}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Additional Actions (Optional) */}
        <div className="mt-8 flex justify-end">
          <Button className="bg-blue-600 hover:bg-blue-700">Register Team</Button>
        </div>
      </div>
    </>
  );
};

export default TournamentPage;
