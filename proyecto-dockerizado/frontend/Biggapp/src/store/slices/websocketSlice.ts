import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import type { WsMessagePayload } from '../../lib/websocket/WebSocketService';

interface WebSocketState {
    connected: boolean;
    realtimeMessages: WsMessagePayload[];
}

const initialState: WebSocketState = {
    connected: false,
    realtimeMessages: [],
};

const websocketSlice = createSlice({
    name: 'websocket',
    initialState,
    reducers: {
        setConnected(state, action: PayloadAction<boolean>) {
            state.connected = action.payload;
        },
        messageReceived(state, action: PayloadAction<WsMessagePayload>) {
            state.realtimeMessages.unshift(action.payload);
            if (state.realtimeMessages.length > 50) {
                state.realtimeMessages.pop();
            }
        },
        clearRealtimeMessages(state) {
            state.realtimeMessages = [];
        },
    },
});

export const { setConnected, messageReceived, clearRealtimeMessages } = websocketSlice.actions;
export default websocketSlice.reducer;
