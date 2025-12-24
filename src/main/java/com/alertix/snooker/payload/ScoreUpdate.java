package com.alertix.snooker.payload;

public class ScoreUpdate {
    private Long matchId;
    private int frameNumber;
    private int scorePlayer1;
    private int scorePlayer2;
    private int currentBreak;
    private Long currentBreakPlayerId;

    public ScoreUpdate() {
    }

    public ScoreUpdate(Long matchId, int frameNumber, int scorePlayer1, int scorePlayer2, int currentBreak,
            Long currentBreakPlayerId) {
        this.matchId = matchId;
        this.frameNumber = frameNumber;
        this.scorePlayer1 = scorePlayer1;
        this.scorePlayer2 = scorePlayer2;
        this.currentBreak = currentBreak;
        this.currentBreakPlayerId = currentBreakPlayerId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public void setScorePlayer1(int scorePlayer1) {
        this.scorePlayer1 = scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }

    public void setScorePlayer2(int scorePlayer2) {
        this.scorePlayer2 = scorePlayer2;
    }

    public int getCurrentBreak() {
        return currentBreak;
    }

    public void setCurrentBreak(int currentBreak) {
        this.currentBreak = currentBreak;
    }

    public Long getCurrentBreakPlayerId() {
        return currentBreakPlayerId;
    }

    public void setCurrentBreakPlayerId(Long currentBreakPlayerId) {
        this.currentBreakPlayerId = currentBreakPlayerId;
    }
}
