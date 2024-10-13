import React from 'react';

interface AvatarProps extends React.HTMLAttributes<HTMLDivElement> {
  src?: string;
}

export const Avatar: React.FC<AvatarProps> = ({ children, className = '', ...props }) => {
  return (
    <div className={`relative inline-block ${className}`} {...props}>
      {children}
    </div>
  );
};

export const AvatarImage: React.FC<React.ImgHTMLAttributes<HTMLImageElement>> = ({ className = '', ...props }) => {
  return (
    <img
      className={`w-10 h-10 rounded-full object-cover ${className}`} // Added object-cover to maintain consistency
      {...props}
    />
  );
};

export const AvatarFallback: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({ children, className = '', ...props }) => {
  return (
    <div
      className={`w-10 h-10 rounded-full bg-gray-600 flex items-center justify-center text-white font-medium ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};