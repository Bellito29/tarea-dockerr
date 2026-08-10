import { createContext, ReactNode, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { loginSuccess, logout, validateToken } from '../store/slices/authSlice';

interface DecodedToken {
    exp?: number;
    username?: string;
    role?: string;
    sub?: string;
}

interface AuthContextType {
    token: string | null;
    user: DecodedToken | null;
    isAuthenticated: boolean;
    login: (newToken: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
    const dispatch = useAppDispatch();
    const { token, user, isAuthenticated } = useAppSelector((state) => state.auth);

    useEffect(() => {
        dispatch(validateToken());
    }, [dispatch]);

    const value: AuthContextType = {
        token,
        user,
        isAuthenticated,
        login: (newToken: string) => {
            dispatch(loginSuccess(newToken));
        },
        logout: () => {
            dispatch(logout());
        },
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthContext;
