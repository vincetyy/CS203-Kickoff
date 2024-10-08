import React, { useState } from 'react';

interface SelectProps {
  children: React.ReactNode;
  onValueChange?: (value: string) => void;
  defaultValue?: string; // Add defaultValue prop
  defaultDisplayValue?: string; // Add defaultDisplayValue prop
}

interface SelectContextType {
  value: string;
  displayValue: string;
  setValue: (value: string, displayValue: string) => void;
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

const SelectContext = React.createContext<SelectContextType | undefined>(undefined);

export const Select: React.FC<SelectProps> = ({ children, 
  onValueChange, 
  defaultValue = '', 
  defaultDisplayValue = '' 
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [value, setValue] = useState(defaultValue);
  const [displayValue, setDisplayValue] = useState(defaultDisplayValue);

  const handleValueChange = (newValue: string, newDisplayValue: string) => {
    setValue(newValue);
    setDisplayValue(newDisplayValue);
    setIsOpen(false);
    if (onValueChange) {
      onValueChange(newValue);
    }
  };

  return (
    <SelectContext.Provider value={{ value, displayValue, setValue: handleValueChange, isOpen, setIsOpen }}>
      <div className="relative">
        {children}
      </div>
    </SelectContext.Provider>
  );
};

interface SelectTriggerProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

export const SelectTrigger: React.FC<SelectTriggerProps> = ({ children, className = '', ...props }) => {
  const context = React.useContext(SelectContext);
  if (!context) throw new Error('SelectTrigger must be used within a Select');

  return (
    <button
      className={`flex justify-between items-center w-full px-4 py-2 text-sm bg-gray-800 border border-gray-700 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-blue-500 ${className}`}
      onClick={() => context.setIsOpen(!context.isOpen)}
      {...props}
    >
      {children}
      <svg className="w-5 h-5 ml-2 -mr-1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
        <path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
      </svg>
    </button>
  );
};

export const SelectValue: React.FC<{ placeholder: string }> = ({ placeholder }) => {
  const context = React.useContext(SelectContext);
  if (!context) throw new Error('SelectValue must be used within a Select');

  return <span>{context.displayValue || placeholder}</span>;
};

export const SelectContent: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({ children, className = '', ...props }) => {
  const context = React.useContext(SelectContext);
  if (!context) throw new Error('SelectContent must be used within a Select');

  if (!context.isOpen) return null;

  return (
    <div className={`absolute z-10 w-full mt-1 bg-gray-800 border border-gray-700 rounded-md shadow-lg ${className}`} {...props}>
      <ul className="py-1">
        {children}
      </ul>
    </div>
  );
};

interface SelectItemProps extends React.LiHTMLAttributes<HTMLLIElement> {
  value: string;
  children: React.ReactNode;
}

export const SelectItem: React.FC<SelectItemProps> = ({ children, className = '', value, ...props }) => {
  const context = React.useContext(SelectContext);
  if (!context) throw new Error('SelectItem must be used within a Select');

  return (
    <li
      className={`block px-4 py-2 text-sm text-gray-300 hover:bg-gray-700 hover:text-white cursor-pointer ${className}`}
      onClick={() => context.setValue(value, children as string)}
      {...props}
    >
      {children}
    </li>
  );
};