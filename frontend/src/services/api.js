import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const patientService = {
  // Patient endpoints
  getAllPatients: () => api.get('/patients'),
  getPatientById: (id) => api.get(`/patients/${id}`),
  createPatient: (patient) => api.post('/patients', patient),
  updatePatient: (id, patient) => api.put(`/patients/${id}`, patient),
  deletePatient: (id) => api.delete(`/patients/${id}`),
};

export const noteService = {
  // Note endpoints
  getPatientNotes: (patientId) => api.get(`/notes/patient/${patientId}`),
  createNote: (note) => api.post('/notes', note),
  updateNote: (id, note) => api.put(`/notes/${id}`, note),
  deleteNote: (id) => api.delete(`/notes/${id}`),
};

export const riskService = {
  // Risk assessment endpoint
  assessRisk: (patientId) => api.get(`/risk/${patientId}`),
};

export default api;

