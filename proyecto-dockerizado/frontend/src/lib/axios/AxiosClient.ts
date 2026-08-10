/// <reference types="vite/client" />
import axios from "axios";
import { store } from "../../store";

const baseURL = import.meta.env.VITE_API_URL;

const axiosClient = axios.create({
    baseURL,
    headers: {
        'Content-Type': 'application/json',
    },
});

axiosClient.interceptors.request.use(config => {
    const state = store.getState() as any;
    const token = state?.auth?.token;
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }

    return config;
});

export default axiosClient;
