import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './utils/axiosSetup'; 
import Layout from './components/Layout';
import LandingPage from './pages/LandingPage';
import ProfilePage from './pages/ProfilePage';
import TournamentsPage from './pages/TournamentsPage';
import ClubPage from './pages/ClubPage';
import LeaderboardsPage from './pages/LeaderboardsPage';
import CreateTournament from './pages/CreateTournament';
import CreateClub from './pages/CreateClub';
import Login from './pages/Login.tsx';
import Signup from './pages/Signup.tsx';

function App() {
  return (
    <Router>
      <Routes>
        {/* No sidebar */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/home" element={<LandingPage />} />
        
        
        {/* Layout with sidebar */}
        <Route element={<Layout />}>
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/tournaments" element={<TournamentsPage />} />
          <Route path="/club" element={<ClubPage />} />
          <Route path="/leaderboards" element={<LeaderboardsPage />} />
          <Route path="/create-tournament" element={<CreateTournament />} />
          <Route path="/clubs/create-club" element={<CreateClub />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
