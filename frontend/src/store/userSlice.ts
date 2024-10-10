import { createSlice } from '@reduxjs/toolkit';

// Define the initial state of the slice
const initialState = {
  userId: null as string | null, // Initially, userId is null
};

// Create the user slice
const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    // Action to set the userId in the state
    setUserId: (state, action) => {
      state.userId = action.payload;
    },
    // Action to clear the userId from the state (e.g., when user logs out)
    clearUserId: (state) => {
      state.userId = null;
    },
  },
});

// Export the actions to set and clear userId
export const { setUserId, clearUserId } = userSlice.actions;

// Selector to retrieve the userId from the Redux store
export const selectUserId = (state: any) => state.user.userId;

// Export the reducer to be included in the Redux store
export default userSlice.reducer;