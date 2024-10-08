import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import LandingPage from './pages/LandingPage';
import ProfilePage from './pages/ProfilePage';
import TournamentsPage from './pages/TournamentsPage';
import ClubPage from './pages/ClubPage';
import LeaderboardsPage from './pages/LeaderboardsPage';
import CreateTournament from './pages/CreateTournament';
import ClubInfo from './pages/ClubInfo';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route element={<Layout />}>
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/tournaments" element={<TournamentsPage />} />
          <Route path="/club" element={<ClubPage />} />
          <Route path="/leaderboards" element={<LeaderboardsPage />} />
          <Route path="/create-tournament" element={<CreateTournament />} />
          <Route path="/club/:id" element={<ClubInfo />} />
        </Route>
      </Routes>
    </Router>
  )
}

export default App
