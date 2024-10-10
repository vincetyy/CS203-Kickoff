import { configureStore, combineReducers } from '@reduxjs/toolkit';
import clubReducer from './clubSlice';
import tournamentReducer from './tournamentSlice';
import userReducer from './userSlice';

// Import redux-persist modules
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
import storage from 'redux-persist/lib/storage'; // defaults to localStorage for web

// Persist configuration for the user slice (userId is likely stored here)
const userPersistConfig = {
  key: 'user',
  storage, // This will use localStorage to persist the state
  whitelist: ['userId'], // Optionally, specify the fields from the user slice that you want to persist
};

// Combine your reducers
const rootReducer = combineReducers({
  clubs: clubReducer,
  tournaments: tournamentReducer,
  user: persistReducer(userPersistConfig, userReducer), // Apply persistence only to the user reducer
});

// Create the persisted reducer
const persistedReducer = persistReducer(
  {
    key: 'root', // Key for overall store persistence
    storage,
    whitelist: ['user'], // Persist only the user slice, not the whole store
  },
  rootReducer
);

// Configure the store
export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER], // Avoid warnings from redux-persist
      },
    }),
});

// Export the persistor for the store
export const persistor = persistStore(store);

// Type exports for the state and dispatch
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;