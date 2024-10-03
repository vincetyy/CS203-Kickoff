import './App.css'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LandingPage from './pages/LandingPage.tsx'; // Import the LandingPage component
import CreateTournament from './pages/CreateTournament';

function App() {

  return (
    <Router>
      <Routes>
        {/* Set the home page to LandingPage */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/create-tournament" element={<CreateTournament />} />
      </Routes>
    </Router>
  )
}

export default App
