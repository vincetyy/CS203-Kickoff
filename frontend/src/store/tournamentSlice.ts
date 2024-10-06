import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchTournaments, joinTournament, createTournament } from '../services/tournamentService';
import { Tournament } from '../types/tournament';

export const fetchTournamentsAsync = createAsyncThunk(
  'tournaments/fetchTournaments',
  async () => {
    return await fetchTournaments();
  }
);

export const joinTournamentAsync = createAsyncThunk(
  'tournaments/joinTournament',
  async ({ clubId, tournamentId }: { clubId: number, tournamentId: number }) => {
    return await joinTournament(clubId, tournamentId);
  }
);

export const createTournamentAsync = createAsyncThunk(
  'tournaments/createTournament',
  async (tournamentData: Partial<Tournament>) => {
    return await createTournament(tournamentData);
  }
);

const tournamentSlice = createSlice({
  name: 'tournaments',
  initialState: {
    tournaments: [] as Tournament[],
    status: 'idle',
    error: null as string | null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchTournamentsAsync.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchTournamentsAsync.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.tournaments = action.payload;
      })
      .addCase(fetchTournamentsAsync.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message || null;
      })
      .addCase(joinTournamentAsync.fulfilled, (state, action) => {
        // Handle successful join if needed
      })
      .addCase(createTournamentAsync.fulfilled, (state, action) => {
        state.tournaments.push(action.payload);
      });
  },
});

export default tournamentSlice.reducer;