import React from 'react';
import { PlayerPosition } from '../types/profile';
import { Badge } from "./ui/Badge"

interface PlayerProfileCardProps {
  id: number;
  name: string;
  preferredPosition?: PlayerPosition;
  availability: boolean;
  isCurrentUser: boolean; 
}

const PlayerProfileCard: React.FC<PlayerProfileCardProps> = ({ id, name, preferredPosition, availability }) => {
  const formatPosition = (position?: string) => {
    if (!position) return 'No position specified';
    return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
  };

  return (
    <div className="bg-gray-800 rounded-lg p-4 flex items-center space-x-4 w-96"> {/* Increased the width to w-96 */}
      <img
        src={`https://picsum.photos/seed/${id}/100/100`}
        alt={`${name}'s profile`}
        className="w-16 h-16 rounded-full object-cover"
      />
      <div className="flex-grow">
        <h3 className="text-lg font-semibold">{name}</h3>
        <p className="text-sm text-gray-400">{formatPosition(preferredPosition)}</p>
      </div>
      <Badge 
        variant={availability ? "success" : "destructive"}
        className="w-24 justify-center whitespace-nowrap"
      >
        {availability ? "Available" : "Not Available"}
      </Badge>
    </div>
  );
};

export default PlayerProfileCard;