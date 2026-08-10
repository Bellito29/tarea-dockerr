import axiosClient from "../../../lib/axios/AxiosClient";

export async function login(username: string, password: string): Promise<Record<string, unknown>> {
    try {
        const response = await axiosClient.post('/public/auth/login', { username, password });
        return response.data;
    } catch (error) {
        console.error('Login failed:', error);
        throw error;
    }
}
