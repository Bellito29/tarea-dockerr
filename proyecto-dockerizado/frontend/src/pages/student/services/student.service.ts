import axiosClient from '../../../lib/axios/AxiosClient';

export const getRoutines = () => axiosClient.get('/routines').then((res) => res.data);

export const getExercises = () => axiosClient.get('/exercises').then((res) => res.data);

export const getSesions = () => axiosClient.get('/sesions').then((res) => res.data);

export const getStats = () => axiosClient.get('/stats').then((res) => res.data);

export const getEvents = () => axiosClient.get('/events').then((res) => res.data);

export const getMessages = () => axiosClient.get('/messages').then((res) => res.data);

export const createMessage = (data: unknown) => axiosClient.post('/messages', data).then((res) => res.data);

export const getNotifications = () => axiosClient.get('/notifications').then((res) => res.data);

export const getRecomendations = () => axiosClient.get('/recomendations').then((res) => res.data);