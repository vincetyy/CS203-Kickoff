package com.crashcourse.kickoff.tms.player.dto;
import java.util.List;
import com.crashcourse.kickoff.tms.player.PlayerPosition;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
public class PlayerProfileResponseDTO {
    private Long id;
    private String username;
    private String profileDescription;
    private List<PlayerPosition> preferredPositions;
}
