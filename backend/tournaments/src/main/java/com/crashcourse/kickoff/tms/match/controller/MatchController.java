package com.crashcourse.kickoff.tms.match.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.crashcourse.kickoff.tms.match.dto.MatchUpdateDTO;

/**
 * REST Controller for managing Matches.
 * Provides endpoints to create, retrieve, update, delete, and list matches.
 */
@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
}
