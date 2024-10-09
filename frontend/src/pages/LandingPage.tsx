import React, { useState, useRef, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import videoSource from '../assets/Kickoff2.mp4';
import aboutImage from '../assets/LandingPageWallpaper.jpg';
import { Helmet } from 'react-helmet';

const LandingPage: React.FC = () => {
  const [videoEnded, setVideoEnded] = useState(false);
  const [showAbout, setShowAbout] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    const video = videoRef.current;
    if (video) {
      video.addEventListener('ended', () => setVideoEnded(true));
      return () => video.removeEventListener('ended', () => setVideoEnded(true));
    }
  }, []);

  return (
    <div className="min-h-screen flex flex-col bg-gray-900 text-white">
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
      <main className="flex-grow relative overflow-hidden">
        <video
          ref={videoRef}
          className="absolute inset-0 w-full h-full object-cover"
          src={videoSource}
          autoPlay
          muted
          playsInline
        />
        <AnimatePresence>
          {videoEnded && (
            <motion.div 
              className="absolute inset-y-0 right-0 w-1/2 flex flex-col justify-center items-start p-8 bg-gray-900 bg-opacity-50"
              initial={{ x: "100%" }}
              animate={{ x: 0 }}
              transition={{ type: "spring", stiffness: 100, damping: 20 }}
            >
              <motion.h2 
                className="text-3xl md:text-7xl font-bold mb-4 leading-tight"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.2, duration: 0.5 }}
              >
                Your Game,<br />Our Passion
              </motion.h2>
              <motion.p
                className="text-left text-xl md:text-2xl mb-8"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.4, duration: 0.5 }}
              >
                Connecting Singapore's football clubs and players through seamless, community-driven tournament management.
              </motion.p>
              <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.6, duration: 0.5 }}
              >
                <Link to="/login">
                  <button 
                    className="bg-blue-600 text-white px-8 py-3 rounded-full text-lg font-semibold hover:bg-blue-700 transition-colors duration-300 glow-button"
                  >
                    Get Started
                  </button>
                </Link>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>
      </main>
    </div>
  );
};

export default LandingPage;