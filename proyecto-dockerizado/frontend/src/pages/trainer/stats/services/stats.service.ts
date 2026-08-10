import axiosClient from "../../../../lib/axios/AxiosClient";

export const getStats = () => axiosClient.get('/stats').then(r => r.data);
export const getStatsById = (id: number) => axiosClient.get(`/stats/${id}`).then(r => r.data);
