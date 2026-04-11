import axios from 'axios';

// Replace with your Laptop's IP for Mobile testing, or localhost for Dev
const API_BASE_URL = 'http://localhost:8080/api/compost';

export const getStatus = () => axios.get(`${API_BASE_URL}/status`);
export const addWaste = (weight) => axios.post(`${API_BASE_URL}/add-waste?weight=${weight}`);
export const collectCompost = () => axios.post(`${API_BASE_URL}/collect`);