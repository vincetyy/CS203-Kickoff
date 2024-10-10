import { useState } from 'react'
import { useDispatch } from 'react-redux'
import { createTournamentAsync } from '../store/tournamentSlice'
import { AppDispatch } from '../store'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "./ui/dialog"
import { toast } from 'react-hot-toast'

interface CreateTournamentProps {
  isOpen: boolean
  onClose: (open: boolean) => void
}

interface NewTournament {
  name: string
  startDateTime: string
  endDateTime: string
  locationId: string
  maxTeams: number
  tournamentFormat: string
  knockoutFormat: string
  minRank: number
  maxRank: number
}

const CreateTournament: React.FC<CreateTournamentProps> = ({ isOpen, onClose }) => {
  const dispatch = useDispatch<AppDispatch>()
  const [newTournament, setNewTournament] = useState<NewTournament>({
    name: '',
    startDateTime: '',
    endDateTime: '',
    locationId: '',
    maxTeams: 0,
    tournamentFormat: '',
    knockoutFormat: '',
    minRank: 0,
    maxRank: 0,
  })

  const handleCreateTournament = async () => {
    console.log(newTournament)
    const { name, startDateTime, endDateTime, locationId, maxTeams, tournamentFormat, knockoutFormat } = newTournament

    if (!name || !startDateTime || !endDateTime || !locationId || !maxTeams || !tournamentFormat || !knockoutFormat) {
      toast.error('Please fill in all required fields', {
        duration: 3000,
        position: 'top-center',
      })
      return
    }

    onClose(false) // Close the dialog immediately

    try {
      await dispatch(createTournamentAsync(newTournament)).unwrap()
      
      // Show the success toast
      toast.success('Tournament created successfully!', {
        duration: 3000,
        position: 'top-center',
      })

      // Reset the form
      setNewTournament({
        name: '',
        startDateTime: '',
        endDateTime: '',
        locationId: '',
        maxTeams: 0,
        tournamentFormat: '',
        knockoutFormat: '',
        minRank: 0,
        maxRank: 0,
      })

    } catch (err: any) {
      console.error('Error creating tournament:', err)
      toast.error(`Failed to create tournament: ${err.message}`, {
        duration: 4000,
        position: 'top-center',
      })
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setNewTournament(prev => ({ ...prev, [name]: value }))
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-[600px] lg:max-w-[800px]">
        <DialogHeader>
          <DialogTitle>Create New Tournament</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="name" className="form-label">Tournament Name</label>
              <Input
                id="name"
                name="name"
                value={newTournament.name}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>
            <div>
              <label htmlFor="locationId" className="form-label">Location ID</label>
              <Input
                id="locationId"
                name="locationId"
                type="number"
                value={newTournament.locationId}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>
            <div>
              <label htmlFor="startDateTime" className="form-label">Start Date & Time</label>
              <Input
                id="startDateTime"
                name="startDateTime"
                type="datetime-local"
                value={newTournament.startDateTime}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>
            <div>
              <label htmlFor="endDateTime" className="form-label">End Date & Time</label>
              <Input
                id="endDateTime"
                name="endDateTime"
                type="datetime-local"
                value={newTournament.endDateTime}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>
            <div>
              <label htmlFor="maxTeams" className="form-label">Max Teams</label>
              <Input
                id="maxTeams"
                name="maxTeams"
                type="number"
                value={newTournament.maxTeams}
                onChange={handleInputChange}
                className="form-input"
                required
              />
            </div>
            <div>
              <label htmlFor="tournamentFormat" className="form-label">Tournament Format</label>
              <Select
                onValueChange={(value) => setNewTournament(prev => ({ ...prev, tournamentFormat: value }))}
              >
                <SelectTrigger className="select-trigger">
                  <SelectValue placeholder="Select format" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="FIVE_SIDE">Five-a-side</SelectItem>
                  <SelectItem value="SEVEN_SIDE">Seven-a-side</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <label htmlFor="knockoutFormat" className="form-label">Knockout Format</label>
              <Select
                onValueChange={(value) => setNewTournament(prev => ({ ...prev, knockoutFormat: value }))}
              >
                <SelectTrigger className="select-trigger">
                  <SelectValue placeholder="Select format" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="SINGLE_ELIM">Single Elimination</SelectItem>
                  <SelectItem value="DOUBLE_ELIM">Double Elimination</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <label htmlFor="minRank" className="form-label">Min Rank</label>
              <Input
                id="minRank"
                name="minRank"
                type="number"
                value={newTournament.minRank}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>
            <div>
              <label htmlFor="maxRank" className="form-label">Max Rank</label>
              <Input
                id="maxRank"
                name="maxRank"
                type="number"
                value={newTournament.maxRank}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>
          </div>
          <div className="flex justify-end space-x-2 mt-6">
            <Button type="button" onClick={() => onClose(false)} className="bg-gray-600 hover:bg-gray-700">
              Cancel
            </Button>
            <Button type="button" onClick={handleCreateTournament} className="bg-blue-600 hover:bg-blue-700">
              Create
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}

export default CreateTournament
