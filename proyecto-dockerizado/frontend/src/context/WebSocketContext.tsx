import { createContext, useContext, useEffect, type ReactNode } from 'react';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { webSocketService } from '../lib/websocket/WebSocketService';
import { setConnected, messageReceived } from '../store/slices/websocketSlice';

interface WebSocketContextValue {
    connected: boolean;
}

const WebSocketContext = createContext<WebSocketContextValue>({ connected: false });

export function WebSocketProvider({ children }: { children: ReactNode }) {
    const dispatch = useAppDispatch();
    const { token, isAuthenticated } = useAppSelector((s) => s.auth);
    const connected = useAppSelector((s) => s.websocket.connected);

    useEffect(() => {
        if (!isAuthenticated || !token) {
            webSocketService.disconnect();
            dispatch(setConnected(false));
            return;
        }

        webSocketService.connect(token);
        dispatch(setConnected(true));

        const unsubscribe = webSocketService.onMessage((payload) => {
            dispatch(messageReceived(payload));
        });

        return () => {
            unsubscribe();
        };
    }, [isAuthenticated, token, dispatch]);

    return (
        <WebSocketContext.Provider value={{ connected }}>
            {children}
        </WebSocketContext.Provider>
    );
}

export function useWebSocket() {
    return useContext(WebSocketContext);
}
