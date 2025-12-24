package com.alertix.snooker.payload.request;

import com.alertix.snooker.domain.MatchEventType;

public class EventRequest {
    private Long frameId;
    private Long playerId;
    private MatchEventType type;
    private int points;

    private String details;

    public Long getFrameId() {
        return frameId;
    }

    public void setFrameId(Long frameId) {
        this.frameId = frameId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public MatchEventType getType() {
        return type;
    }

    public void setType(MatchEventType type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
