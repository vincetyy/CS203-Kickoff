import signupBackground from '@/assets/loginpage_background.png'; // change
import eyePassword from '@/assets/eyePassword.svg';
import eyePasswordOff from '@/assets/eyePasswordOff.svg';

import {
    Input,
    Switch,
    Field,
    Label
} from "@headlessui/react";
import React from "react";
import axios from 'axios';

// Enums for PlayerPosition
enum PlayerPosition {
    POSITION_FORWARD = "POSITION_FORWARD",
    POSITION_MIDFIELDER = "POSITION_MIDFIELDER",
    POSITION_DEFENDER = "POSITION_DEFENDER",
    POSITION_GOALKEEPER = "POSITION_GOALKEEPER"
}

const Signup = () => {
    const [username, setUsername] = React.useState('');
    const [email, setEmail] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [confirmPassword, setConfirmPassword] = React.useState('');
    const [showPassword, setShowPassword] = React.useState(false);
    const [preferredPositions, setPreferredPositions] = React.useState<PlayerPosition[]>([]);
    const [role, setRole] = React.useState(''); // For role selection

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const handlePreferredPositionsChange = (position: PlayerPosition) => {
        setPreferredPositions((prevPositions) =>
            prevPositions.includes(position)
                ? prevPositions.filter((pos) => pos !== position)
                : [...prevPositions, position]
        );
    };

    const handleSignup = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (password !== confirmPassword) {
            alert('Passwords do not match');
            return;
        }

        const payload = {
            username,
            email,
            password,
            preferredPositions,
            role,
        };

        try {
            console.log(payload);
            const response = await axios.post('http://localhost:8081/users', payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 201) {
                console.log('Signup successful:', response.data);
                // Redirect user to login page or dashboard
            }
        } catch (error: unknown) {
            if (axios.isAxiosError(error)) {
                console.error('Error response:', error.response?.data);
            } else {
                console.error('Unknown error:', error);
            }
        }
    };

    // Utility function to format position names
    const formatPosition = (position: string) => {
        return position.replace('POSITION_', '').charAt(0) + position.replace('POSITION_', '').slice(1).toLowerCase();
    };

    return (
        <div className="flex h-screen">
            {/* Left section for the background image */}
            <div className="w-1/2 flex items-center justify-center bg-black relative">
                <div className="text-white text-center">
                    <img
                        className="w-3/4 mx-auto rounded-lg"
                        src={signupBackground}
                        alt="Background"
                    />
                    <h1 className="absolute text-6xl top-1/3 left-1/4 text-white font-bold">
                        Join Us
                    </h1>
                </div>
            </div>

            {/* Right section for signup form */}
            <div className="w-1/2 bg-white flex items-center justify-center">
                <div className="max-w-sm w-full space-y-8 p-8">
                    <div className="text-center">
                        <h2 className="mt-6 text-3xl font-extrabold text-gray-900">
                            Create your account
                        </h2>
                    </div>

                    {/* Signup Form */}
                    <form className="mt-8 space-y-6" onSubmit={handleSignup}>
                        <div className="rounded-md shadow-sm space-y-4">
                            <Field className="max-w-md">
                                <Label htmlFor="username" className="block text-sm font-medium text-gray-700 text-left mb-1">
                                    Username
                                </Label>
                                <Input
                                    id="username"
                                    name="username"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    required
                                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                                    placeholder="Username"
                                />
                            </Field>

                            <Field className="max-w-md">
                                <Label htmlFor="email" className="block text-sm font-medium text-gray-700 text-left mb-1">
                                    Email
                                </Label>
                                <Input
                                    id="email"
                                    name="email"
                                    type="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                                    placeholder="Email"
                                />
                            </Field>

                            <div>
                                <label htmlFor="password" className="block text-sm font-medium text-gray-700 text-left mb-1">
                                    Password
                                </label>
                                <div className="relative">
                                    <input
                                        id="password"
                                        name="password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        type={showPassword ? "text" : "password"}
                                        required
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                                        placeholder="Enter password"
                                    />
                                    <div
                                        className="absolute inset-y-0 right-0 flex items-center px-4 text-gray-600 cursor-pointer"
                                        onClick={togglePasswordVisibility}
                                    >
                                        <img
                                            src={showPassword ? eyePasswordOff : eyePassword}
                                            alt="Toggle password visibility"
                                            className="h-5 w-5"
                                        />
                                    </div>
                                </div>
                            </div>

                            <div>
                                <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 text-left mb-1">
                                    Confirm Password
                                </label>
                                <input
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    type={showPassword ? "text" : "password"}
                                    required
                                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                                    placeholder="Confirm password"
                                />
                            </div>
                        </div>
                        {/* Preferred positions selection */}
                        <div className="mb-6 text-gray-700">
                            <h2 className="text-xl font-semibold">Preferred Positions</h2>
                            <div className="flex flex-wrap">
                                {Object.values(PlayerPosition).map((position) => (
                                    <label key={position} className="mr-4 mb-2 flex items-center">
                                        <input
                                            type="checkbox"
                                            checked={preferredPositions.includes(position)}
                                            onChange={() => handlePreferredPositionsChange(position)}
                                            className="form-checkbox h-4 w-4 text-blue-600"
                                        />
                                        <span className="ml-2">{formatPosition(position)}</span>
                                    </label>
                                ))}
                            </div>
                        </div>

                        {/* Role Selection */}
                        <div className="mt-4">
                            <label className="block text-sm font-medium text-gray-700 text-left mb-1">
                                I am signing up as a
                            </label>
                            <select
                                id="role"
                                name="role"
                                value={role}
                                onChange={(e) => setRole(e.target.value)}
                                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                                required
                            >
                                <option value="" disabled>Select role</option>
                                <option value="player">Player</option>
                                <option value="host">Host</option>
                            </select>
                        </div>
                        {/* Sign Up Button */}
                        <div>
                            <button
                                type="submit"
                                className="w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                            >
                                Sign up
                            </button>
                        </div>

                        <div className="text-center text-sm mt-2">
                            Or sign up with
                            <button className="text-indigo-600 ml-1">
                                <img
                                    src="/assets/google-icon.png" // Replace with Google icon
                                    alt="Google"
                                    className="inline-block w-5 h-5 mr-2"
                                />
                                Google
                            </button>
                        </div>

                        <div className="text-center text-sm">
                            Already have an account?{" "}
                            <a href="/login" className="text-indigo-600">
                                Log in now
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Signup;