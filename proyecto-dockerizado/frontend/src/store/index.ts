import { configureStore, combineReducers } from '@reduxjs/toolkit';
import {
    persistStore,
    persistReducer,
    FLUSH,
    REHYDRATE,
    PAUSE,
    PERSIST,
    PURGE,
    REGISTER,
} from 'redux-persist';
import type { WebStorage } from 'redux-persist';

const storage: WebStorage = {
    getItem: (key: string) => Promise.resolve(localStorage.getItem(key)),
    setItem: (key: string, value: string) => {
        localStorage.setItem(key, value);
        return Promise.resolve();
    },
    removeItem: (key: string) => {
        localStorage.removeItem(key);
        return Promise.resolve();
    },
};
import authReducer from './slices/authSlice';
import uiReducer from './slices/uiSlice';
import websocketReducer from './slices/websocketSlice';

const rootReducer = combineReducers({
    auth: authReducer,
    ui: uiReducer,
    websocket: websocketReducer,
});

const persistConfig = {
    key: 'root',
    storage,
    whitelist: ['auth'], // solo auth se persiste, ui y websocket se reinician al recargar
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
            },
        }),
});

export const persistor = persistStore(store);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
