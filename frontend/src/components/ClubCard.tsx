import React from 'react';
import { Button } from "./ui/button"

interface ClubCardProps {
  id: number;
  name: string;
  description: string;
  ratings: string;
  image: string;
  applied: boolean;
  onClick: () => void;
  // Remove the children prop if you no longer need it
}

const ClubCard: React.FC<ClubCardProps> = ({
  name,
  description,
  ratings,
  image,
  onClick,
}) => {
  return (
    <div
      onClick={onClick}
      className="cursor-pointer bg-gray-800 rounded-lg overflow-hidden shadow-md flex flex-col"
    >
      {/* Card Content */}
      <img src={image} alt={name} className="w-full h-48 object-cover" />
      <div className="p-4 flex-grow">
        <h3 className="text-xl font-bold mb-2">{name}</h3>
        <p className="text-gray-400">{description}</p>
        <p className="text-gray-400 mt-2">{ratings}</p>
        {/* You can remove the Join button from here */}
      </div>
      
    </div>
  );
};

export default ClubCard;