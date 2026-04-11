import React, { useState, useEffect } from 'react';
import axios from 'axios'; // Make sure to npm install axios
import { Play, Thermometer, Gauge, Weight, CheckCircle2, AlertCircle } from 'lucide-react';
import { motion } from 'framer-motion';
import { useNotifications } from '../Notifications/NotificationSystem';

const Dashboard = () => {
  const { addNotification } = useNotifications();
  const [wasteAmount, setWasteAmount] = useState('');
  const [loading, setLoading] = useState(false);

  // State for your Java Backend Data
  const [data, setData] = useState({
    latestSensor: null,
    activeBatch: null
  });

  // 1. Fetch data from Spring Boot
  const fetchUpdate = async () => {
    try {
      // Replace localhost with your IP if testing on a phone
      const response = await axios.get('http://localhost:8080/api/compost/status');
      setData(response.data);
    } catch (err) {
      console.error("Backend unreachable. Check if Spring Boot is running.");
    }
  };

  // 2. Poll the backend every 3 seconds for real-time updates
  useEffect(() => {
    fetchUpdate();
    const interval = setInterval(fetchUpdate, 3000);
    return () => clearInterval(interval);
  }, []);

  // 3. Handle Start Process
  const startProcess = async (e) => {
    e.preventDefault();
    if (!wasteAmount || wasteAmount <= 0) return;

    setLoading(true);
    try {
      await axios.post(`http://localhost:8080/api/compost/add-waste?weight=${wasteAmount}`);
      addNotification(`Process started for ${wasteAmount}kg of waste.`, 'info');
      setWasteAmount('');
      fetchUpdate();
    } catch (err) {
      addNotification("Failed to start process", "error");
    } finally {
      setLoading(false);
    }
  };

  // Variables for easier JSX reading
  const sensor = data.latestSensor;
  const batch = data.activeBatch;
  const progress = batch?.progress || 0;

  return (
    <div className="fade-in">
      <div className="grid">
        {/* Input Section */}
        <section className="glass-panel" style={{ padding: '24px' }}>
          <h3 style={{ marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Weight size={20} color="var(--accent-primary)" />
            New Compost Batch
          </h3>

          {(!batch || batch.status === 'COLLECTED') ? (
            <form onSubmit={startProcess}>
              <div style={{ marginBottom: '16px' }}>
                <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.9rem', color: 'var(--text-secondary)' }}>
                  Total waste collected (kg)
                </label>
                <input
                  type="number"
                  className="input-field"
                  placeholder="e.g. 5.0"
                  value={wasteAmount}
                  onChange={(e) => setWasteAmount(e.target.value)}
                  disabled={loading}
                  required
                />
              </div>
              <button type="submit" className="btn-primary" style={{ width: '100%' }} disabled={loading}>
                <Play size={18} />
                {loading ? 'Starting...' : 'Start Composting'}
              </button>
            </form>
          ) : (
            <div style={{ textAlign: 'center', padding: '20px' }}>
              <CheckCircle2 size={40} color="var(--success)" style={{ marginBottom: '12px' }} />
              <p style={{ color: 'var(--text-secondary)' }}>A batch is currently {batch.status.toLowerCase()}.</p>
              <p style={{ fontWeight: 'bold', color: 'var(--accent-primary)' }}>{batch.initialWeight}kg in progress</p>
            </div>
          )}
        </section>

        {/* Process Tracking (Circle Progress) */}
        <section className="glass-panel" style={{ padding: '24px', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
          <h3 style={{ alignSelf: 'flex-start', marginBottom: '20px' }}>Progress</h3>

          {batch ? (
            <div style={{ textAlign: 'center' }}>
              <div className="circular-progress" style={{ margin: '0 auto 20px' }}>
                <svg viewBox="0 0 100 100">
                  <circle className="bg" cx="50" cy="50" r="45" />
                  <circle
                    className="fg"
                    cx="50"
                    cy="50"
                    r="45"
                    style={{
                      strokeDasharray: 283,
                      strokeDashoffset: 283 - (283 * progress) / 100
                    }}
                  />
                </svg>
                <div style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', fontWeight: 'bold' }}>
                  {Math.round(progress)}%
                </div>
              </div>
              <div style={{ color: 'var(--accent-primary)', fontWeight: '600', fontSize: '0.9rem' }}>
                STATUS: {batch.status}
              </div>
              <div style={{ fontSize: '0.8rem', color: 'var(--warning)', marginTop: '5px' }}>
                {batch.alertMessage}
              </div>
            </div>
          ) : (
            <p style={{ color: 'var(--text-secondary)' }}>No active batch</p>
          )}
        </section>
      </div>

      {/* Real-Time Sensors from Java Backend */}
      {sensor && (
        <motion.section
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="glass-panel"
          style={{ marginTop: '20px', padding: '24px' }}
        >
          <h3 style={{ marginBottom: '24px' }}>Hardware Sensor Levels</h3>
          <div className="grid">
            {/* Temperature */}
            <div className="glass-panel" style={{ padding: '16px', background: 'rgba(255,255,255,0.03)', border: 'none' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                <span style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Temperature</span>
                <Thermometer size={18} color="#ff7b72" />
              </div>
              <div style={{ fontSize: '1.8rem', fontWeight: 'bold' }}>{sensor.temperature}°C</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Optimal: 40°C - 60°C</div>
            </div>

            {/* Gas Level */}
            <div className="glass-panel" style={{ padding: '16px', background: 'rgba(255,255,255,0.03)', border: 'none' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                <span style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Methane Gas</span>
                <Gauge size={18} color={sensor.gasLevel > 500 ? 'var(--danger)' : 'var(--success)'} />
              </div>
              <div style={{ fontSize: '1.8rem', fontWeight: 'bold' }}>{sensor.gasLevel} <small>ppm</small></div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Limit: 500 ppm</div>
            </div>

            {/* Current Weight */}
            <div className="glass-panel" style={{ padding: '16px', background: 'rgba(255,255,255,0.03)', border: 'none' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                <span style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Live Weight</span>
                <Weight size={18} color="#70d7ff" />
              </div>
              <div style={{ fontSize: '1.8rem', fontWeight: 'bold' }}>{sensor.weight}kg</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Monitoring loss...</div>
            </div>
          </div>
        </motion.section>
      )}
    </div>
  );
};

export default Dashboard;