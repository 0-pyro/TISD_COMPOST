import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate, useLocation, Link } from 'react-router-dom';
// Removed Firebase imports
import Login from './components/Auth/Login';
import Dashboard from './components/Dashboard/Dashboard';
import RequestForm from './components/Request/RequestForm';
import BatchHistory from './components/History/BatchHistory';
import Header from './components/Layout/Header';
import { LayoutDashboard, Bell, HandHelping } from 'lucide-react';
import { useNotifications } from './components/Notifications/NotificationSystem';

function App() {
  const { addNotification } = useNotifications();
  const location = useLocation();

  // 1. Hardcode a mock user to skip the Firebase Login screen
  const [user, setUser] = useState({
    email: 'admin@compost.com',
    name: 'Admin User'
  });

  const [loading, setLoading] = useState(false); // No more waiting for Firebase

  const handleLogout = () => {
    // Simply clear the state to "log out" locally
    setUser(null);
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#0a0c10' }}>
        <div style={{ color: '#3fb950', fontSize: '24px', fontWeight: 'bold' }}>Connecting...</div>
      </div>
    );
  }

  return (
    <div className="app-container">
      {user && <Header user={user} onLogout={handleLogout} />}

      <main className="container" style={{ paddingTop: user ? '20px' : '0' }}>
        {user && (
          <nav className="tabs">
            <Link to="/" className={`tab ${location.pathname === '/' ? 'active' : ''}`}>
              <LayoutDashboard size={20} style={{ marginRight: '8px', verticalAlign: 'middle' }} />
              Dashboard
            </Link>
            <Link to="/history" className={`tab ${location.pathname === '/history' ? 'active' : ''}`}>
              <Bell size={20} style={{ marginRight: '8px', verticalAlign: 'middle' }} />
              Notifications
            </Link>
            <Link to="/request" className={`tab ${location.pathname === '/request' ? 'active' : ''}`}>
              <HandHelping size={20} style={{ marginRight: '8px', verticalAlign: 'middle' }} />
              Request
            </Link>
          </nav>
        )}

        <Routes>
          {/* If user exists, go to Dashboard. If not, go to Login */}
          <Route path="/login" element={!user ? <Login /> : <Navigate to="/" />} />
          <Route path="/" element={user ? <Dashboard /> : <Navigate to="/login" />} />
          <Route path="/history" element={user ? <BatchHistory /> : <Navigate to="/login" />} />
          <Route path="/request" element={user ? <RequestForm user={user} /> : <Navigate to="/login" />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;