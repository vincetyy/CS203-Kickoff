package com.crashcourse.kickoff.tms.club.dto;

import lombok.Getter;
import lombok.Setter;

import com.crashcourse.kickoff.tms.user.model.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class CaptainTransferRequest {

    @Valid
    @NotNull(message = "Current captain is required")
    private Long currentCaptainId;

    @Valid
    @NotNull(message = "New captain is required")
    private Long newCaptainId;
}