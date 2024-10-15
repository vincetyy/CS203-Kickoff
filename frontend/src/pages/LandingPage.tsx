import React, { useState, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import videoSource from '../assets/Kickoff2.mp4';

const LandingPage: React.FC = () => {
  const [videoEnded, setVideoEnded] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const video = videoRef.current;
    if (video) {
      video.addEventListener('ended', () => setVideoEnded(true));
      return () => video.removeEventListener('ended', () => setVideoEnded(true));
    }
  }, []);

  const handleVideoClick = () => {
    navigate('/profile'); // Navigate to the login page
  };

  return (
    <div className="flex-1 flex flex-col bg-gray-900 text-white">
      <main className=" flex items-center justify-center">
        <video
          ref={videoRef}
          className="absolute inset-0 w-full h-full object-cover"
          src={videoSource}
          autoPlay
          muted
          playsInline
          onClick={handleVideoClick}
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