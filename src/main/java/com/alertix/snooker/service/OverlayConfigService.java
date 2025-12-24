package com.alertix.snooker.service;

import com.alertix.snooker.domain.OverlayConfig;

public interface OverlayConfigService {
    OverlayConfig getOverlayConfig(Long tournamentId);

    OverlayConfig saveOrUpdateOverlayConfig(Long tournamentId, OverlayConfig config);
}
