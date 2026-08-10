import axiosClient from "../../../../lib/axios/AxiosClient";

export const getExercises = () => axiosClient.get('/exercises').then(r => r.data);
export const getExerciseById = (id: number) => axiosClient.get(`/exercises/${id}`).then(r => r.data);
export const createExercise = (data: unknown) => axiosClient.post('/exercises', data).then(r => r.data);
export const updateExercise = (id: number, data: unknown) => axiosClient.put(`/exercises/${id}`, data).then(r => r.data);
export const deleteExercise = (id: number) => axiosClient.delete(`/exercises/${id}`);
