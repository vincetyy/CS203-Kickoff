import { configureStore } from '@reduxjs/toolkit';
import clubReducer from './clubSlice';
import tournamentReducer from './tournamentSlice';

export const store = configureStore({
  reducer: {
    clubs: clubReducer,
    tournaments: tournamentReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;