package com.alertix.snooker.payload.request;

import com.alertix.snooker.domain.MatchStatus;

public class MatchStatusRequest {
    private Long matchId;
    private MatchStatus status;

    public MatchStatusRequest() {
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }
}
