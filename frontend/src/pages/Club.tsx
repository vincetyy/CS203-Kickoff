import React, { useEffect } from 'react';
import { fetchUserClubAsync } from '../store/userSlice';
import { fetchClubsAsync } from '../store/clubSlice';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../store';
import ClubPage from './ClubPage';
import ClubDashboard from './ClubDashboard';


const Club: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { userClub } = useSelector((state: RootState) => state.user);
  
  useEffect(() => {
    dispatch(fetchClubsAsync());
    dispatch(fetchUserClubAsync());
  }, [dispatch]);
  return (
    <div> 
    { !userClub && 
      <ClubPage /> 
    }
    { userClub && 
      <ClubDashboard id={userClub.id} /> 
    }
    </div>
  );
};

export default Club;
