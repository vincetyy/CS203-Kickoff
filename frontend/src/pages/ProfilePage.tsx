import { useSelector } from 'react-redux';
import { selectUserId} from '../store/userSlice';
import ViewProfile from '../components/ViewProfile';
import Login from '../components/Login';

export default function PlayerProfilePage() {
  const userId = useSelector(selectUserId);


  // Conditional rendering for login or profile page
  if (!userId) {
    return (
      <Login />
    );
  }

  return (<ViewProfile />);
}
