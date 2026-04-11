import React from 'react';
import { LogOut, User, Leaf } from 'lucide-react';

const Header = ({ user, onLogout }) => {
  return (
    <header className="glass-panel" style={{ 
      margin: '0 0 20px 0', 
      padding: '12px 24px', 
      display: 'flex', 
      justifyContent: 'space-between', 
      alignItems: 'center',
      borderRadius: '0 0 16px 16px',
      borderTop: 'none'
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
        <div style={{ 
          background: 'var(--accent-primary)', 
          padding: '6px', 
          borderRadius: '8px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center'
        }}>
          <Leaf color="white" size={24} />
        </div>
        <h2 style={{ fontSize: '1.4rem' }}>EcoCompost</h2>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--text-secondary)' }}>
          <User size={18} />
          <span style={{ fontSize: '0.9rem' }}>{user.email}</span>
        </div>
        <button 
          onClick={onLogout}
          className="btn-primary" 
          style={{ background: 'transparent', border: '1px solid var(--border-color)', padding: '6px 12px' }}
        >
          <LogOut size={16} />
          <span>Logout</span>
        </button>
      </div>
    </header>
  );
};

export default Header;
