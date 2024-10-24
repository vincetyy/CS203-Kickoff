import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import LandingPage from './pages/LandingPage';
import ProfilePage from './pages/ProfilePage';
import TournamentsPage from './pages/TournamentsPage';
import TournamentPage from './pages/TournamentPage';
import Club from './pages/Club';
import SignupPage from './pages/SignupPage';
import ClubInfo from './pages/ClubInfo';
import PlayerApplication from './pages/PlayerApplication';
import LandingLayout from './components/LandingLayout';
import "./utils/axiosSetup.js";
import EditProfile from './pages/EditProfile';
import ViewProfile from './components/ViewProfile';
import AdminProfile from './components/AdminProfile';
import AdminClub from './components/AdminClub';
import AdminTournament from './components/AdminTournament';

function App() {
  return (
    <Router>
      <Routes>
        <Route element={<LandingLayout />}>
          <Route path="/" element={<LandingPage />} />
        </Route>

        <Route element={<Layout />}>
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/profile/edit" element={<EditProfile />} />
          <Route path="/player/:id" element={<ViewProfile />} />
          <Route path="/profile/signup" element={<SignupPage />} />
          <Route path="/tournaments" element={<TournamentsPage />} />
          <Route path="/tournaments/:id" element={<TournamentPage />} />
          <Route path="/clubs" element={<Club />} />
          <Route path="/clubs/:id" element={<ClubInfo />} />
          <Route path="/clubs/:id/applications" element={<PlayerApplication />} />
          
          <Route path="/admin/players" element={<AdminProfile />} />
          <Route path="/admin/clubs" element={<AdminClub />} />
          <Route path="/admin/tournaments" element={<AdminTournament />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;