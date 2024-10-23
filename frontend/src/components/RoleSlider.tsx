import React from 'react';

interface SliderProps {
  selected: string;
  onChange: (role: string) => void;
}

export const Slider: React.FC<SliderProps> = ({ selected, onChange }) => {
  return (
    <div className="relative flex flex-col items-center">
      <div className="group flex space-x-4 relative">
        {['Player', 'Host'].map((option) => (
          <div
            key={option}
            onClick={() => onChange(option.toLowerCase())}
            className={`cursor-pointer px-6 py-2 rounded-lg ${selected === option.toLowerCase()
                ? 'bg-blue-600 text-white font-bold'
                : 'bg-gray-300 text-gray-600'
              } transition-colors`}
          >
            {option}
          </div>
        ))}

        {/* Tooltip on the Right */}
        <div className="absolute left-full ml-2 hidden group-hover:flex justify-center">
          <p className="bg-blue-500 text-white text-xs p-2 rounded-md shadow-lg min-w-[300px] max-w-xs text-center">
            Players can host tournaments as recreational hosts, whereas professional hosts cannot play as players.
          </p>
        </div>
      </div>
    </div>
  );
};
