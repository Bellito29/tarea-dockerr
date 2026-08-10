import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface Notification {
    message: string;
    severity: 'success' | 'error' | 'warning' | 'info';
}

interface UIState {
    loading: boolean;
    notification: Notification | null;
}

const initialState: UIState = {
    loading: false,
    notification: null,
};

const uiSlice = createSlice({
    name: 'ui',
    initialState,
    reducers: {
        setLoading(state, action: PayloadAction<boolean>) {
            state.loading = action.payload;
        },
        showNotification(state, action: PayloadAction<Notification>) {
            state.notification = action.payload;
        },
        clearNotification(state) {
            state.notification = null;
        },
    },
});

export const { setLoading, showNotification, clearNotification } = uiSlice.actions;
export default uiSlice.reducer;
