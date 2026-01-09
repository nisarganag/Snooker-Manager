package com.alertix.snooker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alertix.snooker.payload.request.OverlayConfigRequest;
import com.alertix.snooker.payload.response.MessageResponse;
import com.alertix.snooker.payload.response.OverlayConfigResponse;
import com.alertix.snooker.service.OverlayService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/overlays")
public class OverlayConfigController {

    @Autowired
    private OverlayService overlayService;

    /**
     * Get the global overlay configuration
     * Public endpoint - accessible without authentication (needed for OBS Browser
     * Source)
     */
    @GetMapping("/get")
    public ResponseEntity<?> getOverlayConfig() {
        try {
            OverlayConfigResponse config = overlayService.getGlobalConfig();
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(new MessageResponse("No overlay configuration found"));
        }
    }

    /**
     * Update the global overlay configuration
     * Admin only endpoint
     */
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOverlayConfig(@RequestBody OverlayConfigRequest request) {
        try {
            OverlayConfigResponse updatedConfig = overlayService.updateGlobalConfig(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Overlay configuration updated successfully");
            response.put("config", updatedConfig);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Failed to update overlay configuration"));
        }
    }
}
