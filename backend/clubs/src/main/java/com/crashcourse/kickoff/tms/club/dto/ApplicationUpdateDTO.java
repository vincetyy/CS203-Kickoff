package com.crashcourse.kickoff.tms.club.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ApplicationUpdateDTO {

    @Valid
    @NotNull(message = "Status must not be empty")
    private String applicationStatus;
}
