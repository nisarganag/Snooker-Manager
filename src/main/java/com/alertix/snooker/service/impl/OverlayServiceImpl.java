package com.alertix.snooker.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alertix.snooker.domain.OverlayConfig;
import com.alertix.snooker.payload.request.OverlayConfigRequest;
import com.alertix.snooker.payload.response.OverlayConfigResponse;
import com.alertix.snooker.repository.OverlayConfigRepository;
import com.alertix.snooker.service.OverlayService;

@Service
public class OverlayServiceImpl implements OverlayService {

    @Autowired
    private OverlayConfigRepository overlayConfigRepository;

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");
    private static final List<String> VALID_FONTS = Arrays.asList("Rajdhani", "Inter", "Roboto", "Open Sans",
            "Montserrat");
    private static final List<String> VALID_LAYOUTS = Arrays.asList("MODERN", "CLASSIC", "COMPACT");
    private static final List<String> VALID_SPEEDS = Arrays.asList("slow", "normal", "fast");

    @Override
    public OverlayConfigResponse getGlobalConfig() {
        OverlayConfig config = overlayConfigRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    // Create default configuration if none exists
                    OverlayConfig defaultConfig = new OverlayConfig();
                    defaultConfig.setId(1);
                    return overlayConfigRepository.save(defaultConfig);
                });

        return mapToResponse(config);
    }

    @Override
    @Transactional
    public OverlayConfigResponse updateGlobalConfig(OverlayConfigRequest request) {
        // Get existing config or create new one
        OverlayConfig config = overlayConfigRepository.findFirstByOrderByIdAsc()
                .orElse(new OverlayConfig());

        // Update fields if provided (partial update support)
        if (request.getFontFamily() != null) {
            validateFont(request.getFontFamily());
            config.setFontFamily(request.getFontFamily());
        }

        if (request.getPrimaryColor() != null) {
            validateColor(request.getPrimaryColor(), "primaryColor");
            config.setPrimaryColor(request.getPrimaryColor());
        }

        if (request.getSecondaryColor() != null) {
            validateColor(request.getSecondaryColor(), "secondaryColor");
            config.setSecondaryColor(request.getSecondaryColor());
        }

        if (request.getBackgroundColor() != null) {
            validateColor(request.getBackgroundColor(), "backgroundColor");
            config.setBackgroundColor(request.getBackgroundColor());
        }

        if (request.getAccentColor() != null) {
            validateColor(request.getAccentColor(), "accentColor");
            config.setAccentColor(request.getAccentColor());
        }

        if (request.getLayoutPreset() != null) {
            validateLayoutPreset(request.getLayoutPreset());
            config.setLayoutPreset(request.getLayoutPreset().toUpperCase());
        }

        if (request.getShowPlayerPhotos() != null) {
            config.setShowPlayerPhotos(request.getShowPlayerPhotos());
        }

        if (request.getShowFlags() != null) {
            config.setShowFlags(request.getShowFlags());
        }

        if (request.getAnimationSpeed() != null) {
            validateAnimationSpeed(request.getAnimationSpeed());
            config.setAnimationSpeed(request.getAnimationSpeed().toLowerCase());
        }

        config.setUpdatedAt(LocalDateTime.now());
        config.setId(1); // Ensure id is always 1

        OverlayConfig savedConfig = overlayConfigRepository.save(config);
        return mapToResponse(savedConfig);
    }

    private void validateColor(String color, String fieldName) {
        if (!HEX_COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalArgumentException("Invalid color format for " + fieldName + ". Expected format: #RRGGBB");
        }
    }

    private void validateFont(String font) {
        if (!VALID_FONTS.contains(font)) {
            throw new IllegalArgumentException(
                    "Invalid font family. Allowed values: " + String.join(", ", VALID_FONTS));
        }
    }

    private void validateLayoutPreset(String layout) {
        if (!VALID_LAYOUTS.contains(layout.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Invalid layout preset. Allowed values: " + String.join(", ", VALID_LAYOUTS));
        }
    }

    private void validateAnimationSpeed(String speed) {
        if (!VALID_SPEEDS.contains(speed.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Invalid animation speed. Allowed values: " + String.join(", ", VALID_SPEEDS));
        }
    }

    private OverlayConfigResponse mapToResponse(OverlayConfig config) {
        OverlayConfigResponse response = new OverlayConfigResponse();
        response.setFontFamily(config.getFontFamily());
        response.setPrimaryColor(config.getPrimaryColor());
        response.setSecondaryColor(config.getSecondaryColor());
        response.setBackgroundColor(config.getBackgroundColor());
        response.setAccentColor(config.getAccentColor());
        response.setLayoutPreset(config.getLayoutPreset());
        response.setShowPlayerPhotos(config.getShowPlayerPhotos());
        response.setShowFlags(config.getShowFlags());
        response.setAnimationSpeed(config.getAnimationSpeed());
        return response;
    }
}
