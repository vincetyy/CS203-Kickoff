package com.crashcourse.kickoff.tms.clubTest;

import com.crashcourse.kickoff.tms.club.model.Club;
import com.crashcourse.kickoff.tms.club.service.ClubServiceImpl;
import com.crashcourse.kickoff.tms.club.repository.ClubRepository;
import com.crashcourse.kickoff.tms.club.repository.PlayerApplicationRepository;
import com.crashcourse.kickoff.tms.club.dto.PlayerApplicationDTO;
import com.crashcourse.kickoff.tms.club.model.PlayerApplication;
import com.crashcourse.kickoff.tms.club.model.ApplicationStatus;
import com.crashcourse.kickoff.tms.club.exception.ClubAlreadyExistsException;
import com.crashcourse.kickoff.tms.club.exception.ClubNotFoundException;
import com.crashcourse.kickoff.tms.club.exception.PlayerAlreadyAppliedException;
import com.crashcourse.kickoff.tms.player.PlayerPosition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private PlayerApplicationRepository applicationRepository;

    @InjectMocks
    private ClubServiceImpl clubService;

    public ClubServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createClub_ValidClub_CreatedSuccessfully() throws Exception {
        // Arrange
        Club club = new Club();
        club.setName("Unique Club Name");
        club.setPlayers(new ArrayList<>());
        club.setId(1L);

        when(clubRepository.findByName("Unique Club Name")).thenReturn(Optional.empty());
        when(clubRepository.save(any(Club.class))).thenReturn(club);
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));

        // Act
        Club createdClub = clubService.createClub(club, 1L);

        // Assert
        assertNotNull(createdClub);
        assertEquals("Unique Club Name", createdClub.getName());
        assertEquals(1L, createdClub.getCaptainId());
        verify(clubRepository, times(3)).save(any(Club.class)); // Called 3x in createClub cuz addPlayerToClub also saves
    }

    @Test
    public void createClub_DuplicateName_ThrowsException() {
        // Arrange
        Club club = new Club();
        club.setName("Existing Club Name");

        when(clubRepository.findByName("Existing Club Name")).thenReturn(Optional.of(new Club()));

        // Act
        try {
            clubService.createClub(club, 1L);
            fail("Expected ClubAlreadyExistsException to be thrown");
        } catch (Exception e) {
            // Assert
            assertTrue(e instanceof ClubAlreadyExistsException);
            assertEquals("Club name must be unique", e.getMessage());
        }
    }

    @Test
    public void getClubById_ExistingId_ReturnsClub() {
        // Arrange
        Club club = new Club();
        club.setId(1L);
        club.setName("Test Club");

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));

        // Act
        Optional<Club> result = clubService.getClubById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Club", result.get().getName());
    }

    @Test
    public void getClubById_NonExistingId_ThrowsException() {
        // Arrange
        when(clubRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        try {
            clubService.getClubById(1L);
            fail("Expected ClubNotFoundException to be thrown");
        } catch (Exception e) {
            // Assert
            assertTrue(e instanceof ClubNotFoundException);
            assertEquals("Club with ID 1 not found", e.getMessage());
        }
    }

    @Test
    public void deleteClub_ExistingId_DeletesClubSuccessfully() {
        // Arrange
        Long clubId = 1L;
        when(clubRepository.existsById(clubId)).thenReturn(true);

        // Act
        try {
            clubService.deleteClub(clubId);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Assert
        verify(clubRepository, times(1)).deleteById(clubId);
    }

    @Test
    public void deleteClub_NonExistingId_ThrowsClubNotFoundException() {
        // Arrange
        Long clubId = 1L;
        when(clubRepository.existsById(clubId)).thenReturn(false);

        // Act
        try {
            clubService.deleteClub(clubId);
            fail("Expected ClubNotFoundException to be thrown");
        } catch (Exception e) {
            // Assert
            assertTrue(e instanceof ClubNotFoundException);
            assertEquals("Club with ID 1 not found", e.getMessage());
        }

        // Verify that deleteById is not called
        verify(clubRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void applyToClub_Success_ApplicationSaved() throws Exception {
        // Arrange
        Club club = new Club();
        club.setId(1L);
        club.setPlayers(new ArrayList<>());

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(applicationRepository.existsByPlayerIdAndClub(1L, club)).thenReturn(false);

        PlayerApplicationDTO applicationDTO = new PlayerApplicationDTO();
        applicationDTO.setClubId(1L);
        applicationDTO.setPlayerId(1L);
        applicationDTO.setDesiredPosition(PlayerPosition.POSITION_FORWARD);

        // Act
        clubService.applyToClub(applicationDTO);

        // Assert
        verify(applicationRepository, times(1)).save(any(PlayerApplication.class));
    }

    @Test
    public void applyToClub_PlayerAlreadyApplied_ExceptionThrown() {
        // Arrange
        Club club = new Club();
        club.setId(1L);
        club.setPlayers(new ArrayList<>());

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(applicationRepository.existsByPlayerIdAndClub(1L, club)).thenReturn(true);

        PlayerApplicationDTO applicationDTO = new PlayerApplicationDTO();
        applicationDTO.setClubId(1L);
        applicationDTO.setPlayerId(1L);

        // Act
        try {
            clubService.applyToClub(applicationDTO);
            fail("Expected PlayerAlreadyAppliedException to be thrown");
        } catch (Exception e) {
            // Assert
            assertTrue(e instanceof PlayerAlreadyAppliedException);
            assertEquals("Player has already applied to this club", e.getMessage());
        }
    }
}
