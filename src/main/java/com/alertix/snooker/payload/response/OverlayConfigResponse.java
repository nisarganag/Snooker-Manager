package com.alertix.snooker.payload.response;

public class OverlayConfigResponse {
    private String fontFamily;
    private String primaryColor;
    private String secondaryColor;
    private String backgroundColor;
    private String accentColor;
    private String layoutPreset;
    private Boolean showPlayerPhotos;
    private Boolean showFlags;
    private String animationSpeed;

    public OverlayConfigResponse() {
    }

    // Getters and Setters
    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public String getLayoutPreset() {
        return layoutPreset;
    }

    public void setLayoutPreset(String layoutPreset) {
        this.layoutPreset = layoutPreset;
    }

    public Boolean getShowPlayerPhotos() {
        return showPlayerPhotos;
    }

    public void setShowPlayerPhotos(Boolean showPlayerPhotos) {
        this.showPlayerPhotos = showPlayerPhotos;
    }

    public Boolean getShowFlags() {
        return showFlags;
    }

    public void setShowFlags(Boolean showFlags) {
        this.showFlags = showFlags;
    }

    public String getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(String animationSpeed) {
        this.animationSpeed = animationSpeed;
    }
}
