import './App.css'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LandingPage from './pages/LandingPage.tsx'; // Import the LandingPage component
import Login from './pages/Login.tsx'; 
import Signup from './pages/Signup.tsx';

function App() {

  return (
    <Router>
      <Routes>
        {/* Set the home page to LandingPage */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
      </Routes>
    </Router>
  )
}

export default App
