import React, { useState } from 'react';

interface SheetProps {
  children: React.ReactNode;
  open?: boolean;
  onOpenChange?: (open: boolean) => void;
}

export const Sheet: React.FC<SheetProps> = ({ children, open, onOpenChange }) => {
  const [isOpen, setIsOpen] = useState(open || false);

  const handleOpenChange = (newOpen: boolean) => {
    setIsOpen(newOpen);
    onOpenChange?.(newOpen);
  };

  return (
    <>
      {React.Children.map(children, (child) => {
        if (React.isValidElement(child)) {
          return React.cloneElement(child as React.ReactElement<any>, { isOpen, setIsOpen: handleOpenChange });
        }
        return child;
      })}
    </>
  );
};

interface SheetTriggerProps {
  children: React.ReactNode;
  asChild?: boolean;
}

export const SheetTrigger: React.FC<SheetTriggerProps> = ({ children, asChild }) => {
  return <>{children}</>;
};

interface SheetContentProps extends React.HTMLAttributes<HTMLDivElement> {
  side?: 'left' | 'right' | 'top' | 'bottom';
  isOpen?: boolean;
  setIsOpen?: (isOpen: boolean) => void;
}

export const SheetContent: React.FC<SheetContentProps> = ({ children, side = 'right', className = '', isOpen, setIsOpen, ...props }) => {
  if (!isOpen) return null;

  const sideClasses = {
    left: 'left-0 top-0 h-full',
    right: 'right-0 top-0 h-full',
    top: 'top-0 left-0 w-full',
    bottom: 'bottom-0 left-0 w-full',
  };

  return (
    <div className="fixed inset-0 z-50 overflow-hidden">
      <div className="absolute inset-0 bg-black bg-opacity-50" onClick={() => setIsOpen?.(false)} />
      <div className={`fixed ${sideClasses[side]} w-full max-w-md bg-gray-900 shadow-xl transition-transform duration-300 ease-in-out ${className}`} {...props}>
        {children}
      </div>
    </div>
  );
};