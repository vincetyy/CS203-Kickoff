import React, { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../components/ui/dialog";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";
import { Club } from '../types/club';
import { toast } from 'react-hot-toast';

interface UpdateTournamentProps {
  isOpen: boolean;
  onClose: () => void;
  initialData: TournamentUpdate;
  onUpdate: (data: TournamentUpdate) => Promise<void>;
}

export interface TournamentUpdate {
    name: string;
    startDateTime: string;
    endDateTime: string;
    locationId: string;
    prizePool?: number[];
    minRank?: number;
    maxRank?: number;
    joinedClubs: Club[];
  }

const UpdateTournament: React.FC<UpdateTournamentProps> = ({ isOpen, onClose, initialData, onUpdate }) => {
  const [formData, setFormData] = useState<TournamentUpdate>(initialData);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    setFormData(initialData);
  }, [initialData]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    setFormData(prev => ({
      ...prev,
      [name]: name.includes('DateTime') ? new Date(value).toISOString() :
               name.includes('Id') ? Number(value) :
               name.includes('Rank') ? Number(value) : value
    }));
  };

  const handleSelectChange = (name: keyof TournamentUpdate, value: string) => {
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async () => {
    console.log(formData);
    // Basic client-side validation
    if (!formData.name || !formData.startDateTime || !formData.endDateTime || !formData.locationId) {
      toast.error('Please fill in all required fields', {
        duration: 3000,
        position: 'top-center',
      });
      return;
    }

    setIsSubmitting(true);
    try {
      await onUpdate(formData);
      onClose();
      toast.success('Tournament updated successfully!', {
        duration: 3000,
        position: 'top-center',
      });
    } catch (error: any) {
      console.error('Error updating tournament:', error);
      toast.error(`Failed to update tournament: ${error.message}`, {
        duration: 4000,
        position: 'top-center',
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-[600px] lg:max-w-[800px]">
        <DialogHeader>
          <DialogTitle>Update Tournament</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Tournament Name */}
            <div>
              <label htmlFor="name" className="form-label">Tournament Name</label>
              <Input
                id="name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>

            {/* Location ID */}
            <div>
              <label htmlFor="locationId" className="form-label">Location ID</label>
              <Input
                id="locationId"
                name="locationId"
                type="number"
                value={formData.locationId}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>

            {/* Start Date & Time */}
            <div>
              <label htmlFor="startDateTime" className="form-label">Start Date & Time</label>
              <Input
                id="startDateTime"
                name="startDateTime"
                type="datetime-local"
                value={formData.startDateTime ? formData.startDateTime.substring(0,16) : ''}
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
                value={formData.endDateTime ? formData.endDateTime.substring(0,16) : ''}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>

            {/* Prize Pool */}
            <div>
              <label htmlFor="prizePool" className="form-label">Prize Pool ($)</label>
              <Input
                id="prizePool"
                name="prizePool"
                type="text"
                value={formData.prizePool ? formData.prizePool.join(',') : ''}
                onChange={(e) => {
                  const values = e.target.value.split(',').map(v => parseFloat(v.trim())).filter(v => !isNaN(v) && v > 0);
                  setFormData(prev => ({ ...prev, prizePool: values.length ? values : [] }));
                }}
                className="form-input"
              />
              <small className="text-gray-400">Enter comma-separated positive numbers.</small>
            </div>

            {/* Min Rank */}
            <div>
              <label htmlFor="minRank" className="form-label">Min Rank</label>
              <Input
                id="minRank"
                name="minRank"
                type="number"
                min="0"
                value={formData.minRank ?? ''}
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
                value={formData.maxRank ?? ''}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>

          </div>

          <div className="flex justify-end space-x-2 mt-6">
            <Button 
              type="button" 
              onClick={onClose} 
              className="bg-gray-600 hover:bg-gray-700" 
              disabled={isSubmitting}
            >
              Cancel
            </Button>
            <Button 
              type="button" 
              onClick={handleSubmit} 
              className="bg-blue-600 hover:bg-blue-700" 
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Updating...' : 'Update'}
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateTournament;
