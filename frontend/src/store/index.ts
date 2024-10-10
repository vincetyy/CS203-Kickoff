import { configureStore } from '@reduxjs/toolkit';
import clubReducer from './clubSlice';
import tournamentReducer from './tournamentSlice';
import userReducer from './userSlice';

export const store = configureStore({
  reducer: {
    clubs: clubReducer,
    tournaments: tournamentReducer,
    user: userReducer
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;