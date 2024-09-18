package com.crashcourse.kickoff.tms.tournament;

/*
 * Stores Club format -
 * Mandatory number of players
 * Maximum number of substitutes (KIV)
 */

public enum TournamentFormat {
    FIVE_SIDE(5),
    SEVEN_SIDE(7),
    ELEVEN_SIDE(11);
    
    private final int clubSize;

    TournamentFormat(int clubSize) {
        this.clubSize = clubSize;
    }

}
