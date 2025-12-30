package com.alertix.snooker.payload.response;

public class YouTubeLiveStreamResponse {
    private String id;
    private String youtubeStreamId;
    private String title;
    private String description;
    private String thumbnail;
    private Long viewerCount;
    private String startTime;
    private Boolean isLive;
    private String channelTitle;
    private PlayerInfo player1;
    private PlayerInfo player2;
    private Boolean parsedTitle;
    private String originalTitle;

    public YouTubeLiveStreamResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYoutubeStreamId() {
        return youtubeStreamId;
    }

    public void setYoutubeStreamId(String youtubeStreamId) {
        this.youtubeStreamId = youtubeStreamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(Long viewerCount) {
        this.viewerCount = viewerCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public PlayerInfo getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerInfo player1) {
        this.player1 = player1;
    }

    public PlayerInfo getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerInfo player2) {
        this.player2 = player2;
    }

    public Boolean getParsedTitle() {
        return parsedTitle;
    }

    public void setParsedTitle(Boolean parsedTitle) {
        this.parsedTitle = parsedTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
}
