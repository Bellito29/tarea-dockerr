import axiosClient from "../../../../lib/axios/AxiosClient";

export const getMessages = () => axiosClient.get('/messages').then(r => r.data);
export const getMessageById = (id: number) => axiosClient.get(`/messages/${id}`).then(r => r.data);
export const createMessage = (data: unknown) => axiosClient.post('/messages', data).then(r => r.data);
export const updateMessage = (id: number, data: unknown) => axiosClient.put(`/messages/${id}`, data).then(r => r.data);
export const deleteMessage = (id: number) => axiosClient.delete(`/messages/${id}`);
