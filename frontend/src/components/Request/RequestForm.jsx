import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { ShoppingBag, Loader2, CheckCircle2, XCircle } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { useNotifications } from '../Notifications/NotificationSystem';

const RequestForm = ({ user }) => {
  const { addNotification } = useNotifications();
  const [requestAmount, setRequestAmount] = useState('');
  const [latestBatch, setLatestBatch] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ text: '', type: '' });
  const [totalDelivered, setTotalDelivered] = useState(0);

  const API_BASE_URL = 'http://localhost:8080/api/batches';

  useEffect(() => {
    const fetchData = async () => {
      try {
        const batchResponse = await axios.get(`${API_BASE_URL}/latest`);
        setLatestBatch(batchResponse.data);

        const statsResponse = await axios.get(`${API_BASE_URL}/stats/delivery`);
        setTotalDelivered(statsResponse.data.totalDelivered || 0);
      } catch (err) {
        console.error("Failed to fetch data:", err);
      }
    };

    fetchData();
    const interval = setInterval(fetchData, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleRequest = async (e) => {
    e.preventDefault();
    if (!requestAmount || isNaN(requestAmount) || requestAmount <= 0) return;

    if (!latestBatch || latestBatch.status !== 'COMPLETED') {
      setMessage({ text: "No completed batch available for request.", type: 'error' });
      return;
    }

    const amount = parseFloat(requestAmount);
    setLoading(true);

    try {
      await axios.post(`${API_BASE_URL}/request`, {
        batchId: latestBatch.id,
        farmerEmail: user.email,
        amount: amount
      });

      addNotification(`Successfully requested ${amount}kg!`, 'success');
      setMessage({ text: `Successfully requested ${amount}kg!`, type: 'success' });

      setLatestBatch(prev => ({ ...prev, available: prev.available - amount }));
      setTotalDelivered(prev => prev + amount);
      setRequestAmount('');

    } catch (err) {
      const errorMsg = err.response?.data?.message || "Request failed. Try again.";
      addNotification(errorMsg, 'error');
      setMessage({ text: errorMsg, type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fade-in">
      <div className="grid">
        <section className="glass-panel" style={{ padding: '24px' }}>
          <h3 style={{ marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <ShoppingBag size={20} color="var(--accent-primary)" />
            Farmer Request
          </h3>

          <div className="stat-card" style={{
            background: 'rgba(255,255,255,0.03)',
            padding: '16px',
            borderRadius: '8px',
            marginBottom: '24px',
            border: '1px solid var(--border-color)'
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Available Compost</span>
              <span style={{ fontWeight: 'bold', color: 'var(--accent-primary)' }}>
                {latestBatch?.status === 'COMPLETED' ? `${latestBatch.available} kg` : '0 kg'}
              </span>
            </div>
            <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
              {latestBatch?.status === 'PROCESSING' ? "(Processing...)" :
                (latestBatch?.available === 0 ? "(Fully distributed)" : "(Ready for request)")}
            </div>
          </div>

          <form onSubmit={handleRequest}>
            <div style={{ marginBottom: '16px' }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.9rem', color: 'var(--text-secondary)' }}>
                Amount to request (kg)
              </label>
              <input
                type="number"
                className="input-field"
                placeholder="e.g. 5"
                value={requestAmount}
                onChange={(e) => setRequestAmount(e.target.value)}
                disabled={loading || !latestBatch || latestBatch.available === 0}
                required
              />
            </div>
            <button
              type="submit"
              className="btn-primary"
              style={{ width: '100%' }}
              disabled={loading || !latestBatch || latestBatch.available === 0 || latestBatch.status !== 'COMPLETED'}
            >
              {loading ? <Loader2 size={18} className="animate-spin" /> : 'Request Compost'}
            </button>
          </form>

          <AnimatePresence>
            {message.text && (
              <motion.div
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: 'auto' }}
                exit={{ opacity: 0, height: 0 }}
                className={`message-box ${message.type}`}
                style={{
                  marginTop: '16px',
                  padding: '12px',
                  borderRadius: '6px',
                  fontSize: '0.9rem',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '8px',
                  background: message.type === 'success' ? 'rgba(63, 185, 80, 0.1)' : 'rgba(248, 81, 73, 0.1)',
                  color: message.type === 'success' ? 'var(--success)' : 'var(--danger)'
                }}
              >
                {message.type === 'success' ? <CheckCircle2 size={16} /> : <XCircle size={16} />}
                {message.text}
              </motion.div>
            )}
          </AnimatePresence>
        </section>

        <section className="glass-panel" style={{ padding: '24px', textAlign: 'center', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
          <h4 style={{ color: 'var(--text-secondary)', marginBottom: '10px' }}>Total Compost Delivered</h4>
          <div style={{ fontSize: '3.5rem', fontWeight: 'bold', color: 'var(--accent-primary)' }}>
            {totalDelivered.toFixed(1)}
            <span style={{ fontSize: '1.2rem', marginLeft: '6px', color: 'var(--text-secondary)' }}>kg</span>
          </div>
        </section>
      </div>
    </div>
  );
};

export default RequestForm;