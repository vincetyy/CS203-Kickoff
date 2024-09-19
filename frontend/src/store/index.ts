import { configureStore } from '@reduxjs/toolkit';

export const store = configureStore({
  reducer: {
    // We'll add reducers here later
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;