import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import LandingPage from './pages/LandingPage';
import ProfilePage from './pages/ProfilePage';
import TournamentsPage from './pages/TournamentsPage';
import TournamentPage from './pages/TournamentPage';
import Club from './pages/Club';
import Signup from './pages/Signup.tsx';
import ClubInfo from './pages/ClubInfo';
import PlayerApplication from './pages/PlayerApplication.tsx';
import LandingLayout from './components/LandingLayout.tsx';
import AdminDashboard from './pages/AdminDashboard.tsx';
import "./utils/axiosSetup.js";

function App() {
  return (
    <Router>
      <Routes>
        {/* Routes for the Landing Page and Auth Pages */}
        <Route element={<LandingLayout />}>
          <Route path="/" element={<LandingPage />} />
        </Route>

        {/* Routes for the Main Application, including Layout */}
        <Route element={<Layout />}>
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/profile/signup" element={<Signup />} />
          <Route path="/tournaments" element={<TournamentsPage />} />
          <Route path="/tournaments/:id" element={<TournamentPage />} />
          <Route path="/clubs" element={<Club />} />
          <Route path="/clubs/:id" element={<ClubInfo />} />
          <Route path="/clubs/:id/applications" element={<PlayerApplication />} />
        </Route>
        <Route path="/admin" element={<AdminDashboard />} /> 
      </Routes>
    </Router>
  );
}

export default App;
