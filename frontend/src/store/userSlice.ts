import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getClubByPlayerId } from '../services/clubService'; // Adjust the path to your service function
import { Club } from '../types/club';
import { fetchAllPlayers } from '../services/userService';
import { PlayerProfile } from '../types/profile';

// Initial state for the user slice
const initialState = {
  userId: null as number | null,
  username: null as string | null,  // New field to store the username
  userClub: null as Club | null,  // Store the user's club
  isAdmin: false as boolean,
  players: [] as PlayerProfile[],
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
      let errorMessage = 'Failed to fetch user club';

      // Check if the error is an AxiosError or similar
      if (typeof error === 'object' && error !== null && 'response' in error) {
        const axiosError = error as { response?: { data?: { message?: string } } };
        errorMessage = axiosError.response?.data?.message || errorMessage;
      } else if (error instanceof Error) {
        // Handle standard Error cases
        errorMessage = error.message || errorMessage;
      }

      return rejectWithValue(errorMessage);
    }
  }
);

// Create the user slice
const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.userId = action.payload.userId;  // Set userId
      state.username = action.payload.username;  // Set username
      state.isAdmin = action.payload.isAdmin;
    },
    clearUser: (state) => {
      state.userId = null;
      state.username = null;  // Clear username
      state.userClub = null;
      state.players = [];  
      state.status = 'idle';
      state.error = null;
      state.isAdmin = false;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUserClubAsync.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchUserClubAsync.fulfilled, (state, action) => {
        state.status = 'succeeded';
        console.log("OK");
        console.log(action.payload);

        state.userClub = action.payload;  // Store the fetched club in the state
      })
      .addCase(fetchUserClubAsync.rejected, (state, action) => {
        state.status = 'failed';
        state.userClub = null;
        state.error = action.payload as string;
      })

      .addCase(fetchAllPlayersAsync.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchAllPlayersAsync.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.players = action.payload; 
      })
      .addCase(fetchAllPlayersAsync.rejected, (state, action) => {
        state.status = 'failed';
        state.players = []; 
        state.error = action.payload as string;
      });
  },
});

// Export the actions and reducer
export const { setUser, clearUser } = userSlice.actions;

// Selector to retrieve the userId and username from the Redux store
export const selectUserId = (state: any) => state.user.userId;
export const selectUsername = (state: any) => state.user.username;  // New selector for username
export const selectUserClub = (state: any) => state.user.userClub;
export const selectIsAdmin = (state: any) => state.user.isAdmin;

export default userSlice.reducer;

export const selectPlayers = (state: any) => state.user.players;

// Async thunk to fetch all players
export const fetchAllPlayersAsync = createAsyncThunk(
  'user/fetchAllPlayers',
  async (_, { rejectWithValue }) => {
    try {
      const players = await fetchAllPlayers(); // Fetch all players from the API
      return players;
    } catch (error) {
      let errorMessage = 'Failed to fetch players';

      if (typeof error === 'object' && error !== null && 'response' in error) {
        const axiosError = error as { response?: { data?: { message?: string } } };
        errorMessage = axiosError.response?.data?.message || errorMessage;
      } else if (error instanceof Error) {
        errorMessage = error.message || errorMessage;
      }

      return rejectWithValue(errorMessage);
    }
  }
);