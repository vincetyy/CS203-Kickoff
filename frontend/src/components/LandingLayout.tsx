import React, { useState } from 'react';
import { Link, Outlet } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import aboutImage from '../assets/LandingPageWallpaper.jpg';
import { Helmet } from 'react-helmet';
import { Toaster } from 'react-hot-toast';

const LandingLayout: React.FC = () => {
  const [showAbout, setShowAbout] = useState(false);

  return (
    <div className="flex flex-col min-h-screen bg-gray-900 text-white">
      <Helmet>
        <title>Kickoff</title>
      </Helmet>
      <header className="bg-gray-900 text-white p-4 flex justify-between items-center z-20 relative">
        <Link to="/" className="text-2xl font-bold ml-2 text-white hover:text-gray-300 transition-colors">
          KICKOFF
        </Link>
        <div 
          className="relative"
          onMouseEnter={() => setShowAbout(true)}
          onMouseLeave={() => setShowAbout(false)}
        >
          <button className="px-4 py-2 bg-transparent border border-white rounded hover:bg-white hover:text-black transition-colors duration-300">
            About Us
          </button>
          <AnimatePresence>
            {showAbout && (
              <motion.div
                className="absolute right-0 mt-2 w-64 bg-gray-900 text-white rounded-lg shadow-lg p-4 text-sm"
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -10 }}
                transition={{ duration: 0.2 }}
              >
                <p className="mb-4 text-left">
                Kickoff is the brainchild of a passionate team of CS203 students from SMU — Joel, Setlin, Sheen, Vince, Yekai, and Zane — on a mission to change how football tournaments are organized and played.
                </p>
                <img src={aboutImage} alt="Football field" className="w-full h-32 object-cover rounded" />
              </motion.div>
            )}
          </AnimatePresence>
        </div>
      </header>
      <main className="flex-1">
        {/* The Outlet will render LandingPage here */}
        <Outlet />
      </main>
    </div>
  );
};


export default LandingLayout;