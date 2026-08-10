import axiosClient from '../../../lib/axios/AxiosClient';

// ── Métricas ──────────────────────────────────────────────
export const getUsers    = () => axiosClient.get('/users');
export const getTrainers = () => axiosClient.get('/trainers');
export const getStudents = () => axiosClient.get('/students');
export const getAdmins   = () => axiosClient.get('/admins');
export const getSesions  = () => axiosClient.get('/sesions');

// ── Sesiones recientes ────────────────────────────────────
export const getRecentSesions = () => axiosClient.get('/sesions');

// ── Rutinas ───────────────────────────────────────────────
export const getRoutines = () => axiosClient.get('/routines');

// ── Notificaciones ────────────────────────────────────────
export const getMessages = () => axiosClient.get('/messages');

export const getUserById  = (id: number) => axiosClient.get(`/users/${id}`).then(r => r.data);
export const createUser   = (data: unknown) => axiosClient.post('/users', data).then(r => r.data);
export const updateUser   = (id: number, data: unknown) => axiosClient.put(`/users/${id}`, data).then(r => r.data);
export const deleteUser   = (id: number) => axiosClient.delete(`/users/${id}`);

export const getRoles       = () => axiosClient.get('/roles');
export const createRole     = (data: unknown) => axiosClient.post('/roles', data);
export const updateRole     = (id: number, data: unknown) => axiosClient.put(`/roles/${id}`, data);
export const deleteRole     = (id: number) => axiosClient.delete(`/roles/${id}`);

export const getPermissions   = () => axiosClient.get('/permissions');
export const createPermission = (data: unknown) => axiosClient.post('/permissions', data);
export const updatePermission = (id: number, data: unknown) => axiosClient.put(`/permissions/${id}`, data);
export const deletePermission = (id: number) => axiosClient.delete(`/permissions/${id}`);
