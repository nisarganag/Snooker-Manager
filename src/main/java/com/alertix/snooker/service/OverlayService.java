package com.alertix.snooker.service;

import com.alertix.snooker.payload.request.OverlayConfigRequest;
import com.alertix.snooker.payload.response.OverlayConfigResponse;

public interface OverlayService {
    /**
     * Get the global overlay configuration
     * 
     * @return OverlayConfigResponse containing the current configuration
     * @throws RuntimeException if no configuration exists
     */
    OverlayConfigResponse getGlobalConfig();

    /**
     * Update the global overlay configuration
     * 
     * @param request The overlay configuration update request
     * @return OverlayConfigResponse containing the updated configuration
     */
    OverlayConfigResponse updateGlobalConfig(OverlayConfigRequest request);
}
