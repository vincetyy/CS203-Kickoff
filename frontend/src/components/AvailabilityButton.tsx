import React from 'react';
import { Button } from './ui/button';

interface AvailabilityButtonProps {
  onAvailabilitySelect: (availability: boolean) => void;
}

const AvailabilityButton: React.FC<AvailabilityButtonProps> = ({
  onAvailabilitySelect,
}) => {
  return (
    <div className="space-y-4">
      <p>Are you available to participate in this tournament?</p>
      <div className="flex space-x-3">
        <Button
          className="bg-green-500 hover:bg-green-600 w-full"
          onClick={() => onAvailabilitySelect(true)}
        >
          Available
        </Button>
        <Button
          className="bg-red-500 hover:bg-red-600 w-full"
          onClick={() => onAvailabilitySelect(false)}
        >
          Not Available
        </Button>
      </div>
    </div>
  );
};

export default AvailabilityButton;
