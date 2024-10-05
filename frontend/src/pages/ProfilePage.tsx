'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Toaster, toast } from 'react-hot-toast'

interface UserProfile {
  id: number
  username: string
  roles: string[]
  playerProfile: null | {
    preferredPosition: string
    profileDescription: string
  }
  hostProfile: null | {
    // Add host profile properties if available
  }
}

export default function ProfilePage() {
  const [profile, setProfile] = useState<UserProfile | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const userId = 1 // Replace this with dynamic user ID

  const fetchUserProfile = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/users/${userId}`, {
        auth: {
          username: 'admin',
          password: 'password',
        },
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        }
      })
      setProfile(response.data)
      setLoading(false)
    } catch (err: any) {
      console.error('Error fetching profile:', err)
      if (err.response) {
        setError(`Error: ${err.response.status} - ${err.response.data?.message || 'Unknown error occurred'}`)
      } else if (err.request) {
        setError('Error: No response from server. Please check your network connection or server.')
      } else {
        setError(`Error: ${err.message}`)
      }
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchUserProfile()
  }, [])

  if (loading) return <div className="flex justify-center items-center h-screen bg-gray-900 text-white">Loading...</div>
  if (error) return <div className="flex justify-center items-center h-screen bg-gray-900 text-red-500">Error: {error}</div>

  return (
    <div className="bg-gray-900 text-white min-h-screen p-8">
      <h1 className="text-3xl font-bold mb-8">Welcome, {profile?.username}</h1>
      <div className="bg-gray-800 rounded-lg p-6">
        <h2 className="text-2xl font-bold mb-6">{profile?.username}'s Profile</h2>
        <div className="flex items-center space-x-4 mb-6">
          <div className="w-20 h-20 bg-gray-700 rounded-full flex items-center justify-center text-3xl font-bold">
            {profile?.username?.charAt(0).toUpperCase()}
          </div>
          <div>
            <h3 className="text-xl font-semibold">{profile?.username}</h3>
            <div className="flex space-x-2 mt-2">
              <button className="px-4 py-2 bg-gray-700 rounded text-sm font-medium">Reset Password</button>
              <button className="px-4 py-2 bg-blue-600 rounded text-sm font-medium">Edit</button>
            </div>
          </div>
        </div>
        <div className="mb-4">
          <h3 className="text-lg font-semibold mb-2">Roles:</h3>
          <div className="flex flex-wrap gap-2">
            {profile?.roles.map((role, index) => (
              <span key={index} className="bg-blue-600 text-white px-3 py-1 rounded-md text-sm font-medium">
                {role}
              </span>
            ))}
            <button className="px-3 py-1 bg-gray-700 rounded-md text-sm font-medium">Edit</button>
          </div>
        </div>
        <div className="mb-4">
          <h3 className="text-lg font-semibold mb-2">Player Profile:</h3>
          {profile?.playerProfile ? (
            <div>
              <p>Preferred Position: {profile.playerProfile.preferredPosition}</p>
              <p>Description: {profile.playerProfile.profileDescription}</p>
            </div>
          ) : (
            <p>Player Profile: null</p>
          )}
        </div>
        <div className="mb-4">
          <h3 className="text-lg font-semibold mb-2">Host Profile:</h3>
          {profile?.hostProfile ? (
            <div>
              {/* Add host profile details here when available */}
              <p>Host Profile details would go here</p>
            </div>
          ) : (
            <p>Host Profile: null</p>
          )}
        </div>
      </div>
      <Toaster />
    </div>
  )
}