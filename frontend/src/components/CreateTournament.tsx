import React, { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { createTournamentAsync } from '../store/tournamentSlice';
import { AppDispatch } from '../store';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "./ui/dialog";
import { toast } from 'react-hot-toast';
import { Location, Tournament } from '../types/tournament';
import { getAllLocations } from '../services/tournamentService';

interface CreateTournamentProps {
  isOpen: boolean;
  onClose: (open: boolean) => void;
}

const CreateTournament: React.FC<CreateTournamentProps> = ({ isOpen, onClose }) => {
  const dispatch = useDispatch<AppDispatch>();

  const [newTournament, setNewTournament] = useState<Tournament>({
    name: '',
    startDateTime: '',
    endDateTime: '',
    location: null,
    maxTeams: 0,
    tournamentFormat: '',
    knockoutFormat: '',
    minRank: 0,
    maxRank: 0,
  });

  const [locations, setLocations] = useState<Location[]>([]);
  const [isLoadingLocations, setIsLoadingLocations] = useState<boolean>(false);
  const [locationsError, setLocationsError] = useState<string | null>(null);

  // Fetch locations when the component mounts or when the dialog opens
  useEffect(() => {
    const fetchLocations = async () => {
      setIsLoadingLocations(true);
      setLocationsError(null);
      try {
        const response = await getAllLocations();
        setLocations(response);
      } catch (error: any) {
        console.error('Error fetching locations:', error);
        setLocationsError('Failed to load locations. Please try again.');
      } finally {
        setIsLoadingLocations(false);
      }
    };

    if (isOpen) {
      fetchLocations();
    }
  }, [isOpen]);

  const handleCreateTournament = async () => {
    const { name, startDateTime, endDateTime, location, maxTeams, tournamentFormat, knockoutFormat } = newTournament;
    console.log(startDateTime);
    
    // Validate required fields
    if (!name || !startDateTime || !endDateTime || !location || !maxTeams || !tournamentFormat || !knockoutFormat) {
      toast.error('Please fill in all required fields', {
        duration: 3000,
        position: 'top-center',
      });
      return;
    }

    // Prepare the payload by extracting locationId
    const payload = {
      name,
      startDateTime,
      endDateTime,
      location,
      maxTeams,
      tournamentFormat,
      knockoutFormat,
      minRank: newTournament.minRank,
      maxRank: newTournament.maxRank,
    };

    try {
      await dispatch(createTournamentAsync(payload)).unwrap();

      // Show the success toast
      toast.success('Tournament created successfully!', {
        duration: 3000,
        position: 'top-center',
      });

      // Reset the form
      setNewTournament({
        name: '',
        startDateTime: '',
        endDateTime: '',
        location: null,
        maxTeams: 0,
        tournamentFormat: '',
        knockoutFormat: '',
        minRank: 0,
        maxRank: 0,
      });

      onClose(false); // Close the dialog after successful creation

    } catch (err: any) {
      console.error('Error creating tournament:', err);
      toast.error(`Failed to create tournament: ${err.message}`, {
        duration: 4000,
        position: 'top-center',
      });
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setNewTournament(prev => ({ ...prev, [name]: value }));
  };

  const handleLocationChange = (selectedLocationId: string) => {
    const selectedLocation = locations.find(loc => loc.id === parseInt(selectedLocationId, 10)) || null;
    setNewTournament(prev => ({ ...prev, location: selectedLocation }));
  };

  return (
    <Dialog open={isOpen} onOpenChange={() => onClose(false)}>
      <DialogContent className="sm:max-w-[600px] lg:max-w-[800px]">
        <DialogHeader>
          <DialogTitle>Create New Tournament</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Tournament Name */}
            <div>
              <label htmlFor="name" className="form-label">Tournament Name</label>
              <Input
                id="name"
                name="name"
                value={newTournament.name}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>

            {/* Location Dropdown */}
            <div>
              <label htmlFor="location" className="form-label">Location</label>
              {isLoadingLocations ? (
                <p>Loading locations...</p>
              ) : locationsError ? (
                <p className="text-red-500">{locationsError}</p>
              ) : (
                <Select
                  value={newTournament.location ? newTournament.location.id.toString() : ''}
                  onValueChange={handleLocationChange}
                >
                  <SelectTrigger className="select-trigger">
                    <SelectValue placeholder="Select location" />
                  </SelectTrigger>
                  <SelectContent>
                    {locations.map(loc => (
                      <SelectItem key={loc.id} value={loc.id.toString()}>
                        {loc.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              )}
            </div>

            {/* Start Date & Time */}
            <div>
              <label htmlFor="startDateTime" className="form-label">Start Date & Time</label>
              <Input
                id="startDateTime"
                name="startDateTime"
                type="datetime-local"
                value={newTournament.startDateTime}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>

            {/* End Date & Time */}
            <div>
              <label htmlFor="endDateTime" className="form-label">End Date & Time</label>
              <Input
                id="endDateTime"
                name="endDateTime"
                type="datetime-local"
                value={newTournament.endDateTime}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>

            {/* Max Teams */}
            <div>
              <label htmlFor="maxTeams" className="form-label">Max Teams</label>
              <Input
                id="maxTeams"
                name="maxTeams"
                type="number"
                value={newTournament.maxTeams}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>

            {/* Tournament Format */}
            <div>
              <label htmlFor="tournamentFormat" className="form-label">Tournament Format</label>
              <Select
                value={newTournament.tournamentFormat}
                onValueChange={(value) => setNewTournament(prev => ({ ...prev, tournamentFormat: value }))}
              >
                <SelectTrigger className="select-trigger">
                  <SelectValue placeholder="Select format" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="FIVE_SIDE">Five-a-side</SelectItem>
                  <SelectItem value="SEVEN_SIDE">Seven-a-side</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Knockout Format */}
            <div>
              <label htmlFor="knockoutFormat" className="form-label">Knockout Format</label>
              <Select
                value={newTournament.knockoutFormat}
                onValueChange={(value) => setNewTournament(prev => ({ ...prev, knockoutFormat: value }))}
              >
                <SelectTrigger className="select-trigger">
                  <SelectValue placeholder="Select format" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="SINGLE_ELIM">Single Elimination</SelectItem>
                  <SelectItem value="DOUBLE_ELIM">Double Elimination</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Min Rank */}
            <div>
              <label htmlFor="minRank" className="form-label">Min Rank</label>
              <Input
                id="minRank"
                name="minRank"
                type="number"
                min="0"
                value={newTournament.minRank}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>

            {/* Max Rank */}
            <div>
              <label htmlFor="maxRank" className="form-label">Max Rank</label>
              <Input
                id="maxRank"
                name="maxRank"
                type="number"
                min="0"
                value={newTournament.maxRank}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-end space-x-2 mt-6">
            <Button type="button" onClick={() => onClose(false)} className="bg-gray-600 hover:bg-gray-700">
              Cancel
            </Button>
            <Button type="button" onClick={handleCreateTournament} className="bg-blue-600 hover:bg-blue-700">
              Create
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default CreateTournament;
