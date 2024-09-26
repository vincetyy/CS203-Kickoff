package com.crashcourse.kickoff.tms.club;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.club.repository.ClubRepository;
import com.crashcourse.kickoff.tms.club.repository.PlayerApplicationRepository;
import com.crashcourse.kickoff.tms.club.dto.PlayerApplicationDTO;
import com.crashcourse.kickoff.tms.club.model.PlayerApplication;
import com.crashcourse.kickoff.tms.club.model.ApplicationStatus;
import com.crashcourse.kickoff.tms.club.exception.PlayerAlreadyAppliedException;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import com.crashcourse.kickoff.tms.user.repository.PlayerProfileRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private PlayerProfileRepository playerProfileRepository;

    @Mock
    private PlayerApplicationRepository applicationRepository;

    @InjectMocks
    private ClubServiceImpl clubService;

    public ClubServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApplyToClubSuccess() throws Exception {
        // Arrange: Mock the club, playerProfile, user, and application repository behavior
        Club club = new Club();
        club.setId(1L);
        club.setPlayers(new ArrayList<>());
        
        PlayerProfile playerProfile = new PlayerProfile();
        User user = new User();
        playerProfile.setUser(user);
        
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(playerProfileRepository.findById(1L)).thenReturn(Optional.of(playerProfile));
        when(applicationRepository.existsByPlayerProfileAndClub(playerProfile, club)).thenReturn(false);

        PlayerApplicationDTO applicationDTO = new PlayerApplicationDTO();
        applicationDTO.setClubId(1L);
        applicationDTO.setPlayerProfileId(1L);
        applicationDTO.setDesiredPosition(PlayerPosition.POSITION_FORWARD);

        // Act: Call the method under test
        clubService.applyToClub(applicationDTO);

        // Assert: Verify that the application was saved
        verify(applicationRepository, times(1)).save(any(PlayerApplication.class));
    }

    @Test
    public void testApplyToClubAlreadyApplied() {
        // Arrange
        Club club = new Club();
        PlayerProfile playerProfile = new PlayerProfile();
        User user = new User();
        playerProfile.setUser(user);

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(playerProfileRepository.findById(1L)).thenReturn(Optional.of(playerProfile));
        when(applicationRepository.existsByPlayerProfileAndClub(playerProfile, club)).thenReturn(true);  // Already applied

        PlayerApplicationDTO applicationDTO = new PlayerApplicationDTO();
        applicationDTO.setClubId(1L);
        applicationDTO.setPlayerProfileId(1L);

        // Act & Assert
        assertThrows(PlayerAlreadyAppliedException.class, () -> {
            clubService.applyToClub(applicationDTO);
        });
    }
}
