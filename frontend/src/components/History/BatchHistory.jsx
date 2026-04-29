import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { History, Calendar, Info, Thermometer, Droplets, Beaker } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

const BatchHistory = () => {
  const [batches, setBatches] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchHistory = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/batches/history');
      setBatches(response.data);
    } catch (err) {
      console.error("Failed to fetch batch history:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHistory();
    const interval = setInterval(fetchHistory, 30000); // Poll every 30s
    return () => clearInterval(interval);
  }, []);

  const formatDate = (dateString) => {
    if (!dateString) return '...';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div className="fade-in">
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '24px' }}>
        <History size={24} color="var(--accent-primary)" />
        <h2>Batch History</h2>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        <AnimatePresence>
          {batches.length === 0 && !loading ? (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="glass-panel"
              style={{ padding: '40px', textAlign: 'center', color: 'var(--text-secondary)' }}
            >
              <Info size={40} style={{ marginBottom: '12px', opacity: 0.3 }} />
              <p>No batches recorded yet.</p>
            </motion.div>
          ) : (
            batches.map((batch) => (
              <motion.div
                key={batch.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="glass-panel"
                style={{ padding: '20px', borderLeft: `4px solid ${batch.status === 'COMPLETED' ? 'var(--success)' : 'var(--warning)'}` }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
                  <div>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '4px' }}>
                      ID: {batch.id}
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <Calendar size={14} color="var(--text-secondary)" />
                      <span style={{ fontSize: '0.9rem' }}>{formatDate(batch.startTime)}</span>
                    </div>
                  </div>
                  <div style={{
                    padding: '4px 10px',
                    borderRadius: '12px',
                    fontSize: '0.75rem',
                    fontWeight: 'bold',
                    background: batch.status === 'COMPLETED' ? 'rgba(63, 185, 80, 0.1)' : 'rgba(210, 153, 34, 0.1)',
                    color: batch.status === 'COMPLETED' ? 'var(--success)' : 'var(--warning)',
                    textTransform: 'uppercase'
                  }}>
                    {batch.status}
                  </div>
                </div>

                <div className="grid" style={{ background: 'rgba(0,0,0,0.2)', padding: '16px', borderRadius: '8px' }}>
                  <div>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Yield</div>
                    <div style={{ fontWeight: '600' }}>{batch.compost} kg</div>
                  </div>
                  <div>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Available</div>
                    <div style={{ fontWeight: 'bold', color: 'var(--accent-primary)' }}>{batch.available} kg</div>
                  </div>
                </div>

              </div>
              </motion.div>
        ))
          )}
      </AnimatePresence>
    </div>
    </div >
  );
};

export default BatchHistory;