package com.alertix.snooker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alertix.snooker.domain.OverlayConfig;
import com.alertix.snooker.payload.request.IdRequest;
import com.alertix.snooker.service.OverlayConfigService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/overlays")
public class OverlayConfigController {

    @Autowired
    private OverlayConfigService overlayConfigService;

    @PostMapping("/get")
    public ResponseEntity<OverlayConfig> getOverlayConfig(@RequestBody IdRequest request) {
        OverlayConfig config = overlayConfigService.getOverlayConfig(request.getId());
        if (config != null) {
            return ResponseEntity.ok(config);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OverlayConfig> updateOverlayConfig(@RequestBody OverlayConfigRequest request) {
        try {
            OverlayConfig config = new OverlayConfig();
            config.setFontFamily(request.getFontFamily());
            config.setPrimaryColor(request.getPrimaryColor());
            config.setSecondaryColor(request.getSecondaryColor());
            config.setLayoutPreset(request.getLayoutPreset());

            return ResponseEntity.ok(overlayConfigService.saveOrUpdateOverlayConfig(request.getTournamentId(), config));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Inner DTO for Request
    public static class OverlayConfigRequest {
        private Long tournamentId;
        private String fontFamily;
        private String primaryColor;
        private String secondaryColor;
        private String layoutPreset;

        public Long getTournamentId() {
            return tournamentId;
        }

        public void setTournamentId(Long tournamentId) {
            this.tournamentId = tournamentId;
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

        public String getLayoutPreset() {
            return layoutPreset;
        }

        public void setLayoutPreset(String layoutPreset) {
            this.layoutPreset = layoutPreset;
        }
    }
}
