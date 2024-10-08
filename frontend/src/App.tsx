import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './utils/axiosSetup'; 
import Layout from './components/Layout';
import LandingPage from './pages/LandingPage';
import ProfilePage from './pages/ProfilePage';
import TournamentsPage from './pages/TournamentsPage';
import TournamentPage from './pages/TournamentPage';
import ClubPage from './pages/ClubPage';
import LeaderboardsPage from './pages/LeaderboardsPage';
import CreateTournament from './pages/CreateTournament';
import Login from './pages/Login.tsx';
import Signup from './pages/Signup.tsx';
import ClubInfo from './pages/ClubInfo';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route element={<Layout />}>
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/tournaments" element={<TournamentsPage />} />
          <Route path="/tournaments/:id" element={<TournamentPage />} />
          <Route path="/club" element={<ClubPage />} />
          <Route path="/leaderboards" element={<LeaderboardsPage />} />
          <Route path="/create-tournament" element={<CreateTournament />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/club/:id" element={<ClubInfo />} />
        </Route>
      </Routes>
    </Router>
  )
}

export default App
