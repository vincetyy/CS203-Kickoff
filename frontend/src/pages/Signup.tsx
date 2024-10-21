import React, { useState } from 'react';
import axios from 'axios';
import { Input } from '../components/ui/input';
import { Button } from '../components/ui/button';
import { PlayerPosition } from '../types/profile';
import { Slider } from '../components/RoleSlider';
import eyePassword from '@/assets/eyePassword.svg';
import eyePasswordOff from '@/assets/eyePasswordOff.svg';
import { toast } from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

export default function Signup() {
    const navigate = useNavigate();


    // States for sign-up form
    const [role, setRole] = useState('player');
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [preferredPositions, setPreferredPositions] = useState<PlayerPosition[]>([]);

    // Toggle password visibility
    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    // Handle preferred positions change
    const handlePreferredPositionsChange = (position: PlayerPosition) => {
        setPreferredPositions((prevPositions) =>
            prevPositions.includes(position)
                ? prevPositions.filter((pos) => pos !== position)
                : [...prevPositions, position]
        );
    };

    // Handle form submission
    const handleSignup = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (password !== confirmPassword) {
            toast.error('Passwords do not match', {
                duration: 3000,
                position: 'top-center',
            });
            return;
        }

        const payload = {
            username,
            email,
            password,
            preferredPositions,
            role,
        };
        console.log(payload);
        try {
            const response = await axios.post('http://localhost:8081/users', payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 201) {
                toast.success('Sign up successful!', {
                    duration: 3000,
                    position: 'top-center',
                });
                navigate('/profile');
            }
        } catch (error: unknown) {
            console.error('Error during sign up:', error);
            toast.error('Failed to sign up. Please try again.', {
                duration: 4000,
                position: 'top-center',
            });
        }
    };

    const formatPosition = (position: string) => {
        return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
    };

    return (
        <div className="flex justify-center bg-gray-900">
            <div className="bg-gray-800 p-8 rounded-lg shadow-lg w-full max-w-md">
                <h2 className="text-3xl font-extrabold text-white text-center mb-6">Sign Up as...</h2>

                <Slider selected={role} onChange={setRole} />
                <form className="space-y-6" onSubmit={handleSignup}>
                    <div className="space-y-4">
                        {/* Username Input */}
                        <div>
                            <label
                                htmlFor="username"
                                className="block text-sm font-medium text-white mb-1"
                            >
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
                            <label htmlFor="email" className="block text-sm font-medium text-white mb-1">Email</label>
                            <Input
                                id="email"
                                name="email"
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                className="w-full"
                                placeholder="Enter Email"
                            />
                        </div>

                        <div>
                            <label htmlFor="password" className="block text-sm font-medium text-white mb-1">Password</label>
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

                        <div>
                            <label htmlFor="confirmPassword" className="block text-sm font-medium text-white mb-1">Confirm Password</label>
                            <Input
                                id="confirmPassword"
                                name="confirmPassword"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                type={showPassword ? 'text' : 'password'}
                                required
                                className="w-full"
                                placeholder="Confirm Password"
                            />
                        </div>
                    </div>

                    {/* Conditional Rendering of Player Positions */}
                    {role === 'player' && (
                        <div className="mb-6">
                            <h2 className="text-xl font-semibold text-white mb-2">Preferred Positions</h2>
                            <div className="flex flex-wrap">
                                {Object.values(PlayerPosition).map((position) => (
                                    <label key={position} className="mr-4 mb-2 flex items-center">
                                        <input
                                            type="checkbox"
                                            checked={preferredPositions.includes(position)}
                                            onChange={() => handlePreferredPositionsChange(position)}
                                            className="form-checkbox h-4 w-4 text-blue-600"
                                        />
                                        <span className="ml-2 text-white">{formatPosition(position)}</span>
                                    </label>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Submit Button */}
                    <Button
                        type="submit"
                        className="w-full bg-blue-600 hover:bg-blue-700"
                    >
                        Sign Up
                    </Button>

                    <div className="text-center text-sm text-white">
                        Already have an account?{' '}
                        <a
                            onClick={() => navigate('/profile/')}
                            className="text-indigo-400 cursor-pointer"
                        >
                            Login now
                        </a>
                    </div>
                </form>
            </div>
        </div>
    );
}
