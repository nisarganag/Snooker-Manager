package com.alertix.snooker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.OverlayConfig;
import com.alertix.snooker.domain.Tournament;
import com.alertix.snooker.repository.OverlayConfigRepository;
import com.alertix.snooker.repository.TournamentRepository;
import com.alertix.snooker.service.OverlayConfigService;

@Service
public class OverlayConfigServiceImpl implements OverlayConfigService {

    @Autowired
    private OverlayConfigRepository overlayConfigRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public OverlayConfig getOverlayConfig(Long tournamentId) {
        if (tournamentId == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }
        return overlayConfigRepository.findByTournamentId(tournamentId).orElse(null);
    }

    @Override
    public OverlayConfig saveOrUpdateOverlayConfig(Long tournamentId, OverlayConfig config) {
        if (tournamentId == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        return overlayConfigRepository.findByTournamentId(tournamentId).map(existing -> {
            existing.setFontFamily(config.getFontFamily());
            existing.setPrimaryColor(config.getPrimaryColor());
            existing.setSecondaryColor(config.getSecondaryColor());
            existing.setLayoutPreset(config.getLayoutPreset());
            return overlayConfigRepository.save(existing);
        }).orElseGet(() -> {
            config.setTournament(tournament);
            return overlayConfigRepository.save(config);
        });
    }
}
