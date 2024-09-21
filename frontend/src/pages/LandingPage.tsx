import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function LandingPage() {
  const [showVideo, setShowVideo] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setShowVideo(false);
    }, 3000);

    document.title = "Kickoff";

    return () => {
      clearTimeout(timer);
    };
  }, []);

  return (
    <div className="min-h-screen flex flex-col w-full bg-gradient-to-br from-green-400 to-blue-500">
      {showVideo ? (
        <video
          autoPlay
          muted
          className="w-full h-screen object-cover"
          onEnded={() => setShowVideo(false)}
        >
          <source src="/goal-video.mp4" type="video/mp4" />
          Your browser does not support the video tag.
        </video>
      ) : (
        <div className="flex flex-col flex-grow w-full">
          {/* Header */}
          <header className="w-full flex justify-between items-center p-6 bg-opacity-80 bg-black">
            <div className="text-3xl md:text-4xl font-bold text-white">Kickoff</div>
            <Link
              to="/about"
              className="bg-white bg-opacity-20 hover:bg-opacity-30 text-white font-semibold py-2 px-4 rounded-full transition duration-300 ease-in-out transform hover:scale-105"
            >
              About Us
            </Link>
          </header>

          {/* Main Content */}
          <main className="flex-grow flex flex-col items-center justify-center text-center p-4 md:p-8 w-full">
            <h1 className="text-4xl md:text-6xl font-bold mb-8 text-white">
              Your passion, our game
            </h1>
            <div className="space-y-6 md:space-y-0 md:space-x-6 md:flex">
              <Link
                to="/create-tournament"
                className="inline-block px-8 py-3 bg-yellow-400 text-gray-800 font-bold rounded-full transition duration-300 ease-in-out transform hover:scale-105 hover:bg-yellow-300 shadow-lg w-full md:w-auto"
              >
                Create a tournament
              </Link>
              <Link
                to="/join-tournament"
                className="inline-block px-8 py-3 bg-purple-600 text-white font-bold rounded-full transition duration-300 ease-in-out transform hover:scale-105 hover:bg-purple-500 shadow-lg w-full md:w-auto"
              >
                Join a tournament
              </Link>
            </div>
          </main>

          {/* Footer */}
          <footer className="w-full text-center py-4 bg-black bg-opacity-70 text-white">
            © {new Date().getFullYear()} Kickoff. All rights reserved.
          </footer>
        </div>
      )}
    </div>
  );
}
