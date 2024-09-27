import React, { useState } from 'react';

interface SelectProps {
  children: React.ReactNode;
}

export const Select: React.FC<SelectProps> = ({ children }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [value, setValue] = useState<string | undefined>(undefined);

  return (
    <div className="relative">
      {React.Children.map(children, (child) => {
        if (React.isValidElement(child)) {
          return React.cloneElement(child as React.ReactElement<any>, { isOpen, setIsOpen, value, setValue });
        }
        return child;
      })}
    </div>
  );
};

interface SelectTriggerProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  isOpen?: boolean;
  setIsOpen?: (isOpen: boolean) => void;
}

export const SelectTrigger: React.FC<SelectTriggerProps> = ({ children, className = '', isOpen, setIsOpen, value, ...props }) => {
  return (
    <button
      className={`flex justify-between items-center w-full px-4 py-2 text-sm bg-gray-800 border border-gray-700 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-blue-500 ${className}`}
      onClick={() => setIsOpen?.(!isOpen)}
      {...props}
    >
      {React.Children.map(children, (child) =>
        React.isValidElement(child) && child.type === SelectValue
          ? React.cloneElement(child, { value })
          : child
      )}
      <svg className="w-5 h-5 ml-2 -mr-1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
        <path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
      </svg>
    </button>
  );
};

export const SelectContent: React.FC<React.HTMLAttributes<HTMLDivElement> & { isOpen?: boolean; setValue?: (value: string) => void }> = ({ children, className = '', isOpen, setValue, ...props }) => {
  if (!isOpen) return null;

  return (
    <div className={`absolute z-10 w-full mt-1 bg-gray-800 border border-gray-700 rounded-md shadow-lg ${className}`} {...props}>
      <ul className="py-1">
        {React.Children.map(children, (child) =>
          React.isValidElement(child) && child.type === SelectItem
            ? React.cloneElement(child, { setValue })
            : child
        )}
      </ul>
    </div>
  );
};

export const SelectItem: React.FC<React.LiHTMLAttributes<HTMLLIElement> & { value: string; setValue?: (value: string) => void }> = ({ children, className = '', value, setValue, ...props }) => {
  return (
    <li
      className={`block px-4 py-2 text-sm text-gray-300 hover:bg-gray-700 hover:text-white cursor-pointer ${className}`}
      onClick={() => setValue?.(value)}
      {...props}
    >
      {children}
    </li>
  );
};

export const SelectValue: React.FC<React.HTMLAttributes<HTMLSpanElement> & { placeholder?: string; value?: string }> = ({ value, placeholder }) => {
  return <span>{value || placeholder}</span>;
};