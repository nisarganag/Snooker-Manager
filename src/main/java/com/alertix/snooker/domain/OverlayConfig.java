package com.alertix.snooker.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "overlay_config")
public class OverlayConfig {

    @Id
    @Column(nullable = false)
    private Integer id = 1; // Always 1 - single global configuration

    @Column(name = "font_family", length = 50)
    private String fontFamily = "Rajdhani";

    @Column(name = "primary_color", length = 7)
    private String primaryColor = "#00ff41";

    @Column(name = "secondary_color", length = 7)
    private String secondaryColor = "#ffffff";

    @Column(name = "background_color", length = 7)
    private String backgroundColor = "#0a0f0a";

    @Column(name = "accent_color", length = 7)
    private String accentColor = "#00cc33";

    @Column(name = "layout_preset", length = 20)
    private String layoutPreset = "MODERN";

    @Column(name = "show_player_photos")
    private Boolean showPlayerPhotos = true;

    @Column(name = "show_flags")
    private Boolean showFlags = true;

    @Column(name = "animation_speed", length = 20)
    private String animationSpeed = "normal";

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public OverlayConfig() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = 1; // Always enforce id = 1
    }

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
