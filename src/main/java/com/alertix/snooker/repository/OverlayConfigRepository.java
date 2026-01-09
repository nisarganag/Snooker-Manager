package com.alertix.snooker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alertix.snooker.domain.OverlayConfig;

@Repository
public interface OverlayConfigRepository extends JpaRepository<OverlayConfig, Integer> {
    /**
     * Find the global overlay configuration (always id = 1)
     * 
     * @return Optional containing the global config if it exists
     */
    Optional<OverlayConfig> findFirstByOrderByIdAsc();
}
