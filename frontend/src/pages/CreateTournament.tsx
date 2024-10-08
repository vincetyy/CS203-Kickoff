import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface CreateTournamentProps {
    tournamentId?: number; // Add this to support updating existing tournaments
    existingTournamentData?: Tournament; // Pass existing tournament data for updates
    isUpdate?: boolean; // Flag to indicate whether it's an update or create
    onClose: () => void; // Function to close the popup
  }

const CreateTournament: React.FC<CreateTournamentProps> = ({ tournamentId, existingTournamentData, isUpdate = false, onClose }) => {
    const [form, setForm] = useState({
        name: '',
        startDateTime: '',
        endDateTime: '',
        locationId: '',
        maxTeams: 0,
        minRank: 0,
        maxRank: 0,
        tournamentFormat: '',
        knockoutFormat: '',
    });
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const locations = [
        { id: 1, name: 'Location A' },
        { id: 2, name: 'Location B' },
        // Add other locations as needed
    ];
    const tournamentFormats = ['FIVE_SIDE', 'SEVEN_SIDE']; // Match backend enums
    const knockoutFormats = ['SINGLE_ELIM', 'DOUBLE_ELIM']; // Match backend enums

    // If updating, prefill the form with existing data
    useEffect(() => {
        if (isUpdate && existingTournamentData) {
            setForm({
                name: existingTournamentData.name || '',
                startDateTime: existingTournamentData.startDateTime || '',
                endDateTime: existingTournamentData.endDateTime || '',
                locationId: existingTournamentData.location?.id?.toString() || '',
                maxTeams: existingTournamentData.maxTeams || 0,
                minRank: existingTournamentData.minRank || 0,
                maxRank: existingTournamentData.maxRank || 0,
                tournamentFormat: existingTournamentData.tournamentFormat || '',
                knockoutFormat: existingTournamentData.knockoutFormat || '',
            });
        }
    }, [isUpdate, existingTournamentData]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);
        try {
            const payload = {
                name: form.name,
                startDateTime: new Date(form.startDateTime).toISOString(),
                endDateTime: new Date(form.endDateTime).toISOString(),
                locationId: Number(form.locationId),
                maxTeams: Number(form.maxTeams),
                minRank: Number(),
                maxRank: Number(),
                tournamentFormat: form.tournamentFormat,
                knockoutFormat: form.knockoutFormat || null,
                // Include other fields if needed
            };

            let response;
            if (isUpdate && tournamentId) {
                // Update tournament with PUT or PATCH
                response = await axios.put(`http://localhost:8080/tournaments/${tournamentId}`, payload, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                setSuccess('Tournament updated successfully!');
            } else {
                // Create new tournament with POST
                response = await axios.post('http://localhost:8080/tournaments', payload, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                setSuccess('Tournament created successfully!');
            }
            console.log('Tournament created/updated:', response.data);
        } catch (error: any) {
            if (error.response && error.response.data) {
                setError(error.response.data.message || 'Failed to create tournament');
            } else {
                setError('An unexpected error occurred');
            }
            console.error('Error creating tournament:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit} noValidate>
            {error && <div style={{ color: 'red' }}>{error}</div>}
            {success && <div style={{ color: 'green' }}>{success}</div>}
            <div>
                <label>Tournament Name:</label>
                <input
                    type="text"
                    name="name"
                    value={form.name}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Start Date and Time:</label>
                <input
                    type="datetime-local"
                    name="startDateTime"
                    value={form.startDateTime}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>End Date and Time:</label>
                <input
                    type="datetime-local"
                    name="endDateTime"
                    value={form.endDateTime}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Location:</label>
                <select
                    name="locationId"
                    value={form.locationId}
                    onChange={handleChange}
                    required
                >
                    <option value="">Select Location</option>
                    {locations.map(loc => (
                        <option key={loc.id} value={loc.id}>
                            {loc.name}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <label>Maximum Teams:</label>
                <input
                    type="number"
                    name="maxTeams"
                    value={form.maxTeams}
                    onChange={handleChange}
                    min="0"
                />
            </div>
            <div>
                <label>Tournament Format:</label>
                <select
                    name="tournamentFormat"
                    value={form.tournamentFormat}
                    onChange={handleChange}
                    required
                >
                    <option value="">Select Format</option>
                    {tournamentFormats.map(format => (
                        <option key={format} value={format}>
                            {format.replace('_', ' ')}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <label>Knockout Format:</label>
                <select
                    name="knockoutFormat"
                    value={form.knockoutFormat}
                    onChange={handleChange}
                >
                    <option value="">Select Knockout Format</option>
                    {knockoutFormats.map(kf => (
                        <option key={kf} value={kf}>
                            {kf.replace('_', ' ')}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <label>Min Rank:</label>
                <input
                    type="number"
                    name="minRank"
                    value={form.minRank}
                    onChange={handleChange}
                    min="0"
                />
                <label>Max Rank:</label>
                <input
                    type="number"
                    name="maxRank"
                    value={form.maxRank}
                    onChange={handleChange}
                    min="0"
                />
            </div>
            <button type="submit">Create Tournament</button>
        </form>
    );
};

export default CreateTournament;
