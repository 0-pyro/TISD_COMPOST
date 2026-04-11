import React, { useState, useEffect } from 'react';
import { db } from '../../firebase/config';
import { 
  collection, 
  query, 
  orderBy, 
  onSnapshot 
} from 'firebase/firestore';
import { History, Calendar, CheckCircle2, Info, Thermometer, Droplets, Beaker } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';


const BatchHistory = () => {
  const [batches, setBatches] = useState([]);

  useEffect(() => {
    const q = query(collection(db, "batches"), orderBy("startTime", "desc"));
    const unsubscribe = onSnapshot(q, (snapshot) => {
      const batchList = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));
      setBatches(batchList);
    });

    return () => unsubscribe();
  }, []);

  const formatDate = (timestamp) => {
    if (!timestamp) return '...';
    const date = timestamp.toDate();
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
        <h2>Batch History & Notifications</h2>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        <AnimatePresence>
          {batches.length === 0 ? (
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="glass-panel" 
              style={{ padding: '40px', textAlign: 'center', color: 'var(--text-secondary)' }}
            >
              <Info size={40} style={{ marginBottom: '12px', opacity: 0.3 }} />
              <p>No batches recorded yet. Start your first composting process on the Dashboard.</p>
            </motion.div>
          ) : (
            batches.map((batch) => (
              <motion.div 
                key={batch.id} 
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, scale: 0.95 }}
                layout
                className="glass-panel" 
                style={{ padding: '20px', borderLeft: `4px solid ${batch.status === 'completed' ? 'var(--success)' : 'var(--warning)'}` }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
                  <div>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '4px' }}>
                      BATCH ID: {batch.id.substring(0, 8)}...
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
                    background: batch.status === 'completed' ? 'rgba(63, 185, 80, 0.1)' : 'rgba(210, 153, 34, 0.1)',
                    color: batch.status === 'completed' ? 'var(--success)' : 'var(--warning)',
                    textTransform: 'uppercase'
                  }}>
                    {batch.status}
                  </div>
                </div>

                <div className="grid" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', background: 'rgba(0,0,0,0.2)', padding: '16px', borderRadius: '8px' }}>
                  <div>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Total Waste</div>
                    <div style={{ fontWeight: '600' }}>{batch.totalWaste} kg</div>
                  </div>
                  <div>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Compost Yield</div>
                    <div style={{ fontWeight: '600' }}>{batch.compost} kg</div>
                  </div>
                  <div>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Available (LIVE)</div>
                    <div style={{ fontWeight: 'bold', color: 'var(--accent-primary)' }}>{batch.available} kg</div>
                  </div>
                </div>

                <div style={{ display: 'flex', gap: '20px', marginTop: '16px', flexWrap: 'wrap' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                    <Thermometer size={14} color="#ff7b72" />
                    <span>Temp: {batch.sensors?.temperature}°C (Opt: 50-60°C)</span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                    <Droplets size={14} color="#70d7ff" />
                    <span>Moisture: {batch.sensors?.moisture}% (Opt: 40-60%)</span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                    <Beaker size={14} color="#a5d6ff" />
                    <span>pH: {batch.sensors?.ph} (Opt: 6.0-8.0)</span>
                  </div>
                </div>

                {batch.status === 'completed' && (
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginTop: '12px', borderTop: '1px solid var(--border-color)', paddingTop: '12px' }}>
                    Finished: {formatDate(batch.endTime)}
                  </div>
                )}
              </motion.div>
            ))
          )}
        </AnimatePresence>
      </div>
    </div>
  );
};

export default BatchHistory;
