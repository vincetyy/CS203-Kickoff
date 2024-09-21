package com.crashcourse.kickoff.tms.club.dto;

import com.crashcourse.kickoff.tms.user.model.*;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class CaptainTransferRequest {

    @Valid
    @NotNull(message = "Current captain is required")
    private User currentCaptain;

    @Valid
    @NotNull(message = "New captain is required")
    private User newCaptain;
}