import axiosClient from "../../../../lib/axios/AxiosClient";

export const getStudents = () => axiosClient.get('/students').then(r => r.data);
export const getStudentById = (id: number) => axiosClient.get(`/students/${id}`).then(r => r.data);
