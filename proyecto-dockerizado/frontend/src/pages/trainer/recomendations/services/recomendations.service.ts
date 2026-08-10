import axiosClient from "../../../../lib/axios/AxiosClient";

export const getRecomendations = () => axiosClient.get('/recomendations').then(r => r.data);
export const getRecomendationById = (id: number) => axiosClient.get(`/recomendations/${id}`).then(r => r.data);
export const createRecomendation = (data: unknown) => axiosClient.post('/recomendations', data).then(r => r.data);
export const updateRecomendation = (id: number, data: unknown) => axiosClient.put(`/recomendations/${id}`, data).then(r => r.data);
export const deleteRecomendation = (id: number) => axiosClient.delete(`/recomendations/${id}`);
