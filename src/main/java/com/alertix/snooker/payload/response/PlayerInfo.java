package com.alertix.snooker.payload.response;

public class PlayerInfo {
    private String name;

    public PlayerInfo() {
    }

    public PlayerInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
