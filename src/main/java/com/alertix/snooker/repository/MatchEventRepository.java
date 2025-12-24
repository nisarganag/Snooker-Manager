package com.alertix.snooker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alertix.snooker.domain.MatchEvent;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    List<MatchEvent> findByFrameIdOrderByTimestampDesc(Long frameId);
}
