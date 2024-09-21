package com.crashcourse.kickoff.tms.club.dto;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.user.User;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClubCreationRequest {

    @Valid
    @NotNull(message = "Club details are required")
    private Club club;

    @Valid
    @NotNull(message = "Creator details are required")
    private User creator;
}