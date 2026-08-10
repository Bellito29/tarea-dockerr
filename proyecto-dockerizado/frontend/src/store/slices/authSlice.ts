import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
    exp?: number;
    username?: string;
    role?: string;
    sub?: string;
}

interface AuthState {
    token: string | null;
    user: DecodedToken | null;
    isAuthenticated: boolean;
}

function decodeToken(token: string): DecodedToken | null {
    try {
        const decoded = jwtDecode<DecodedToken>(token);
        if (decoded?.exp && decoded.exp * 1000 < Date.now()) {
            return null;
        }
        return decoded;
    } catch {
        return null;
    }
}

const initialState: AuthState = {
    token: null,
    user: null,
    isAuthenticated: false,
};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        loginSuccess(state, action: PayloadAction<string>) {
            const user = decodeToken(action.payload);
            if (user) {
                state.token = action.payload;
                state.user = user;
                state.isAuthenticated = true;
            }
        },
        logout(state) {
            state.token = null;
            state.user = null;
            state.isAuthenticated = false;
        },
        validateToken(state) {
            if (state.token) {
                const user = decodeToken(state.token);
                if (!user) {
                    state.token = null;
                    state.user = null;
                    state.isAuthenticated = false;
                }
            }
        },
    },
});

export const { loginSuccess, logout, validateToken } = authSlice.actions;
export default authSlice.reducer;
