import React, { useState, useEffect } from 'react';
import { db } from '../../firebase/config';
import { 
  collection, 
  query, 
  orderBy, 
  limit, 
  onSnapshot, 
  runTransaction, 
  doc, 
  increment 
} from 'firebase/firestore';
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

  useEffect(() => {
    // Listen for latest batch
    const q = query(collection(db, "batches"), orderBy("startTime", "desc"), limit(1));
    const unsubBatch = onSnapshot(q, (snapshot) => {
      if (!snapshot.empty) {
        setLatestBatch({ id: snapshot.docs[0].id, ...snapshot.docs[0].data() });
      }
    });

    // Listen for total delivered stats
    const unsubStats = onSnapshot(doc(db, "stats", "delivery"), (docSnap) => {
      if (docSnap.exists()) {
        setTotalDelivered(docSnap.data().totalDelivered);
      }
    });

    return () => {
      unsubBatch();
      unsubStats();
    };
  }, []);

  const handleRequest = async (e) => {
    e.preventDefault();
    if (!requestAmount || isNaN(requestAmount) || requestAmount <= 0) return;
    if (!latestBatch || latestBatch.status !== 'completed') {
      setMessage({ text: "No completed batch available for request.", type: 'error' });
      return;
    }

    const amount = parseFloat(requestAmount);
    setLoading(true);
    setMessage({ text: '', type: '' });

    try {
      await runTransaction(db, async (transaction) => {
        const batchRef = doc(db, "batches", latestBatch.id);
        const statsRef = doc(db, "stats", "delivery");
        
        // ALL READS FIRST
        const batchDoc = await transaction.get(batchRef);
        if (!batchDoc.exists()) throw "Batch does not exist!";

        const statsDoc = await transaction.get(statsRef);

        const available = batchDoc.data().available;
        if (available < amount) {
          throw `Insufficient compost! Only ${available}kg remaining.`;
        }

        // ALL WRITES AFTER
        // 1. Update batch availability
        transaction.update(batchRef, {
          available: available - amount
        });

        // 2. Create request record
        const requestRef = doc(collection(db, "requests"));
        transaction.set(requestRef, {
          batchId: latestBatch.id,
          farmerName: user.email, 
          amount: amount,
          timestamp: new Date()
        });

        // 3. Update stats
        if (!statsDoc.exists()) {
          transaction.set(statsRef, { totalDelivered: amount });
        } else {
          transaction.update(statsRef, {
            totalDelivered: increment(amount)
          });
        }
      });

      addNotification(`Successfully requested ${amount}kg of compost!`, 'success');
      setMessage({ text: `Successfully requested ${amount}kg of compost!`, type: 'success' });
      setRequestAmount('');
    } catch (err) {
      console.error("Transaction failed: ", err);
      const errorMsg = typeof err === 'string' ? err : "Request failed. Try again.";
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

          <div style={{ 
            background: 'rgba(255,255,255,0.03)', 
            padding: '16px', 
            borderRadius: '8px', 
            marginBottom: '24px',
            border: '1px solid var(--border-color)'
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Available Compost</span>
              <span style={{ fontWeight: 'bold', color: 'var(--accent-primary)' }}>
                {latestBatch?.status === 'completed' ? `${latestBatch.available} kg` : '0 kg'}
              </span>
            </div>
            <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
              {latestBatch?.status === 'processing' ? "(Processing in progress...)" : 
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
              disabled={loading || !latestBatch || latestBatch.available === 0 || latestBatch.status !== 'completed'}
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
          <p style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', marginTop: '10px' }}>
            Contribution to sustainable campus farming
          </p>
        </section>
      </div>
    </div>
  );
};

export default RequestForm;
