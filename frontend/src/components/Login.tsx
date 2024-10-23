import React, { useState} from 'react';
import { Input } from './ui/input';
import { Button } from './ui/button';
import eyePassword from '@/assets/eyePassword.svg';
import eyePasswordOff from '@/assets/eyePasswordOff.svg';
import { toast } from 'react-hot-toast';
import { login} from '../services/userService';
import { useDispatch} from 'react-redux';
import { setUser, fetchUserClubAsync } from '../store/userSlice';
import { AppDispatch } from '../store';
import { useNavigate } from 'react-router-dom';

export default function Login() {
    const dispatch = useDispatch<AppDispatch>();
    const navigate = useNavigate();

    // States for login form
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    // Toggle password visibility
    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    // Handle login form submission
    const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            const response = await login(username, password);

            if (response.status === 200) {
                const token = response.data.jwtToken;
                localStorage.setItem('authToken', token);
                localStorage.setItem('username', username);
                
                dispatch(setUser({ userId: response.data.userId, username: username, isAdmin: response.data.admin }));
                dispatch(fetchUserClubAsync());
                toast.success(`Welcome back, ${username}`, {
                    duration: 3000,
                    position: 'top-center',
                });
            }
        } catch (error) {
            console.error('Error during login:', error);
            toast.error('Invalid username or password', {
                duration: 4000,
                position: 'top-center',
            });
        }
    };
    return (<div className="flex justify-center items-center ">
        <div className="max-w-sm w-full space-y-8 p-8 bg-gray-900 rounded-lg">
            <div className="text-center">
                <h2 className="mt-6 text-3xl text-white">
                    Log in to see your profile.
                </h2>
            </div>

            {/* Login Form */}
            <form className="mt-8 space-y-6" onSubmit={handleLogin}>
                <div className="rounded-md shadow-sm space-y-4">
                    <div>
                        <label htmlFor="username" className="block text-sm font-medium text-white mb-1">
                            Username
                        </label>
                        <Input
                            id="username"
                            name="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            className="w-full"
                            placeholder="Enter Username"
                        />
                    </div>
                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-white mb-1">
                            Password
                        </label>
                        <div className="relative">
                            <Input
                                id="password"
                                name="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                type={showPassword ? 'text' : 'password'}
                                required
                                className="w-full"
                                placeholder="Enter Password"
                            />
                            <div className="absolute inset-y-0 right-0 flex items-center px-4 text-gray-600 cursor-pointer" onClick={togglePasswordVisibility}>
                                <img src={showPassword ? eyePassword : eyePasswordOff} alt="Toggle Password Visibility" className="h-5 w-5" />
                            </div>
                        </div>
                    </div>
                </div>

                <Button type="submit" className="w-full bg-blue-600 hover:bg-blue-700">
                    Sign in
                </Button>

                <div className="text-center text-sm text-white mt-2">
                    Or sign in with Google
                </div>

                <div className="text-center text-sm text-white">
                    Don’t have an account?{' '}
                    <a onClick={() => navigate("/profile/signup")} className="text-indigo-400">
                        Sign up now
                    </a>
                </div>
            </form>
        </div>
    </div>
    )
}