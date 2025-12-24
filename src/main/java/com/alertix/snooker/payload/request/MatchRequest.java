package com.alertix.snooker.payload.request;

import java.time.LocalDateTime;

public class MatchRequest {
    private Long tournamentId;
    private Long player1Id;
    private Long player2Id;
    private Long refereeId;
    private int bestOfFrames;
    private LocalDateTime startTime;

    public MatchRequest() {
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public Long getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(Long refereeId) {
        this.refereeId = refereeId;
    }

    public int getBestOfFrames() {
        return bestOfFrames;
    }

    public void setBestOfFrames(int bestOfFrames) {
        this.bestOfFrames = bestOfFrames;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
