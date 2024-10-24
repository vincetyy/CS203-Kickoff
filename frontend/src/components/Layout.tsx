import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import Header from './Header';
import Sidebar from './Sidebar';
import { selectIsAdmin } from '../store/userSlice';
import { RootState } from '../store';

export default function Layout() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const isAdmin = useSelector((state: RootState) => selectIsAdmin(state));
  const navigate = useNavigate();
  const [hasNavigated, setHasNavigated] = useState(false); // Track navigation state

  useEffect(() => {
    // Redirect to /admin/players only once if the user is an admin
    if (isAdmin && !hasNavigated) {
      setHasNavigated(true); // Mark navigation as done
      navigate('/admin/players'); // Redirect to admin players
    }
  }, [isAdmin, hasNavigated, navigate]);

  return (
    <div className="flex flex-col min-h-screen bg-gray-900 text-white">
      <Header />
      <div className="flex flex-1 overflow-hidden">
        <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />
        <main className="flex-1 p-4 lg:p-6 overflow-auto w-full">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
