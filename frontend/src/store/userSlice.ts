import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getClubByPlayerId } from '../services/clubService'; // Adjust the path to your service function
import { Club } from '../types/club';

// Initial state for the user slice
const initialState = {
  userId: null as number | null,
  userClub: null as Club | null,  // Store the user's club
  status: 'idle' as 'idle' | 'loading' | 'succeeded' | 'failed',
  error: null as string | null,
};

// Async thunk to fetch the user's club by userId
export const fetchUserClubAsync = createAsyncThunk(
  'user/fetchUserClub', // Action type
  async (_, { getState, rejectWithValue }) => {
    const state = getState() as { user: typeof initialState };  // Access the state
    const userId = state.user.userId;

    if (!userId) {
      return rejectWithValue('User is not logged in or userId is missing');
    }

    try {
      const club = await getClubByPlayerId(userId);  // Call the API to get the club
      return club;
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch user club');
    }
  }
);

// Create the user slice
const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUserId: (state, action) => {
      state.userId = action.payload;
    },
    clearUser: (state) => {
      state.userId = null;
      state.userClub = null;
      state.status = 'idle';
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUserClubAsync.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchUserClubAsync.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.userClub = action.payload;  // Store the fetched club in the state
      })
      .addCase(fetchUserClubAsync.rejected, (state, action) => {
        state.status = 'failed';
        state.userClub = null;
        state.error = action.payload as string;
      });
  },
});

// Export the actions and reducer
export const { setUserId, clearUser } = userSlice.actions;
// Selector to retrieve the userId from the Redux store
export const selectUserId = (state: any) => state.user.userId;

export default userSlice.reducer;