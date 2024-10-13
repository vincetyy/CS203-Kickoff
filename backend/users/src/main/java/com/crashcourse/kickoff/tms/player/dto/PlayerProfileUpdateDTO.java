package com.crashcourse.kickoff.tms.player.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerProfileUpdateDTO {
    public String[] preferredPositions;
    public String profileDescription;
}
