package com.crashcourse.kickoff.tms.tournament;

/*
 * Stores Club format -
 * Mandatory number of players
 * Maximum number of substitutes (KIV)
 */

public enum ClubFormat {
    FIVE_SIDE(5),
    SEVEN_SIDE(7),
    ELEVEN_SIDE(11);
    
    private final int clubSize;

    ClubFormat(int clubSize) {
        this.clubSize = clubSize;
    }

}
