import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { fetchTournamentById, fetchTournaments, joinTournament, createTournament, updateTournament, removeClubFromTournament } from '../services/tournamentService';
import { Tournament } from '../types/tournament';

export const fetchTournamentByIdAsync = createAsyncThunk(
  'tournaments/fetchTournamentById',
  async (tournamentId: number) => {
    return await fetchTournamentById(tournamentId);;
  }
);

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

export const updateTournamentAsync = createAsyncThunk(
  'tournaments/updateTournament',
  async ({ tournamentId, tournamentData }: { tournamentId: number; tournamentData: Partial<Tournament> }) => {
    return await updateTournament(tournamentId, tournamentData);
  }
);

export const removeClubFromTournamentAsync = createAsyncThunk(
  'tournaments/removeClubFromTournament',
  async ({ tournamentId, clubId }: { tournamentId: number; clubId: number }) => {
    return await removeClubFromTournament(tournamentId, clubId);
  }
);

const tournamentSlice = createSlice({
  name: 'tournaments',
  initialState: {
    tournaments: [] as Tournament[],
    status: 'idle',
    error: null as string | null,
  },
  reducers: {
    updateTournaments: (state, action: PayloadAction<Tournament[]>) => {
      state.tournaments = action.payload;
    },
  },
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
      }).addCase(updateTournamentAsync.fulfilled, (state, action) => {
        const updatedTournament = action.payload;
        const index = state.tournaments.findIndex(tournament => tournament.id === updatedTournament.id);
        if (index !== -1) {
          state.tournaments[index] = updatedTournament;
        }
      }).addCase(removeClubFromTournamentAsync.fulfilled, (state, action) => {
        const { tournamentId, clubId } = action.meta.arg;  // Get tournamentId and clubId from action
        const tournament = state.tournaments.find(t => t.id === tournamentId);
        if (tournament) {
          tournament.joinedClubsIds = tournament.joinedClubsIds?.filter(club => club !== clubId);
        }
      });
  },
});

export const { updateTournaments } = tournamentSlice.actions;

export default tournamentSlice.reducer;