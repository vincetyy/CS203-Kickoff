import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchClubs, applyToClub } from '../services/clubService';
import { Club } from '../types/club';

export const fetchClubsAsync = createAsyncThunk(
  'clubs/fetchClubs',
  async () => {
    return await fetchClubs();
  }
);

export const applyToClubAsync = createAsyncThunk(
  'clubs/applyToClub',
  async ({ clubId, playerProfileId, desiredPosition }: { clubId: number, playerProfileId: number, desiredPosition: string }) => {
    return await applyToClub(clubId, playerProfileId, desiredPosition);
  }
);

const clubSlice = createSlice({
  name: 'clubs',
  initialState: {
    clubs: [] as Club[],
    status: 'idle',
    error: null as string | null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchClubsAsync.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchClubsAsync.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.clubs = action.payload;
      })
      .addCase(fetchClubsAsync.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message || null;
      })
      .addCase(applyToClubAsync.fulfilled, (state, action) => {
        // Handle successful application if needed
      });
  },
});

export default clubSlice.reducer;