package com.crashcourse.kickoff.tms.club.dto;

import com.crashcourse.kickoff.tms.club.model.Club;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreationRequest {

    @Valid
    @NotNull(message = "Club details are required")
    private Club club;
}