package com.crashcourse.kickoff.tms.tournament.model;

import lombok.*;

/*
 * Handles all the possible knockout formats
 * KIV: LEAGUE, ROUND_ROBIN, PLAYOFFS
 */

public enum KnockoutFormat {
    SINGLE_ELIM, DOUBLE_ELIM;
}
