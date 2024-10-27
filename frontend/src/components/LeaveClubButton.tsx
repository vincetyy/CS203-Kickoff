'use client'

import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../components/ui/dialog'
import { toast } from 'react-hot-toast'
import { fetchUserClubAsync, selectUserId, selectUserClub } from '../store/userSlice'
import { Club } from '../types/club'
import { leaveClub } from '../services/clubService'

export default function LeaveClubButton() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const userId = useSelector(selectUserId)
  const userClub: Club | null = useSelector(selectUserClub)
  const clubId = userClub?.id
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleLeaveClub = async () => {
    if (!userId || !clubId) {
      toast.error('User or club information not available')
      return
    }

    try {
      const response = await leaveClub(clubId, userId)

      if (response.status === 200) {
        if (response.data === "Club has been disbanded.") {
          toast.success('The club has been disbanded as you were the last member')
          navigate('/clubs')
        } else {
          toast.success('You have left the club successfully')
          navigate('/profile')
        }
        dispatch(fetchUserClubAsync() as any)
      } else {
        throw new Error('Failed to leave the club')
      }
    } catch (error: any) {
      console.error('Error leaving club:', error)
      if (error.response) {
        if (error.response.status === 500) {
          setError('An unexpected error occurred on the server. Please try again later.')
        } else if (error.response.data === "You must transfer the captaincy before leaving the club.") {
          setIsDialogOpen(true)
        } else {
          setError(error.response.data || 'An error occurred while trying to leave the club')
        }
      } else {
        setError('An error occurred while trying to leave the club')
      }
    }
  }

  const handleTransferCaptaincy = () => {
    toast('Please transfer captaincy to another player before leaving')
    setIsDialogOpen(false)
  }

  return (
    <>
      <Button 
        onClick={handleLeaveClub}
        className="bg-red-500 hover:bg-red-600 text-white"
      >
        Leave Club
      </Button>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Cannot Leave Club</DialogTitle>
          </DialogHeader>
          <p>
            As the captain, you must transfer captaincy to another player before leaving the club.
          </p>
          <div className="flex justify-end space-x-2 mt-4">
            <Button onClick={handleTransferCaptaincy}>Transfer Captaincy</Button>
            <Button variant="ghost" onClick={() => setIsDialogOpen(false)}>Cancel</Button>
          </div>
        </DialogContent>
      </Dialog>

      {error && (
        <Dialog open={!!error} onOpenChange={() => setError(null)}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Error</DialogTitle>
            </DialogHeader>
            <p>{error}</p>
            <div className="flex justify-end mt-4">
              <Button variant="ghost" onClick={() => setError(null)}>Close</Button>
            </div>
          </DialogContent>
        </Dialog>
      )}
    </>
  )
}