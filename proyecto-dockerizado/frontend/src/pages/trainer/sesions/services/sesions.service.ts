import axiosClient from "../../../../lib/axios/AxiosClient";

export const getSesions = () => axiosClient.get('/sesions').then(r => r.data);
export const getSesionById = (id: number) => axiosClient.get(`/sesions/${id}`).then(r => r.data);
export const createSesion = (data: unknown) => axiosClient.post('/sesions', data).then(r => r.data);
export const updateSesion = (id: number, data: unknown) => axiosClient.put(`/sesions/${id}`, data).then(r => r.data);
export const deleteSesion = (id: number) => axiosClient.delete(`/sesions/${id}`);
