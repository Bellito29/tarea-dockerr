import axiosClient from "../../../../lib/axios/AxiosClient";

export const getRoutines = () => axiosClient.get('/routines').then(r => r.data);
export const getRoutineById = (id: number) => axiosClient.get(`/routines/${id}`).then(r => r.data);
export const createRoutine = (data: unknown) => axiosClient.post('/routines', data).then(r => r.data);
export const updateRoutine = (id: number, data: unknown) => axiosClient.put(`/routines/${id}`, data).then(r => r.data);
export const deleteRoutine = (id: number) => axiosClient.delete(`/routines/${id}`);
