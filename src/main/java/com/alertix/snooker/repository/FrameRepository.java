package com.alertix.snooker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alertix.snooker.domain.Frame;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Long> {
    List<Frame> findByMatchId(Long matchId);
}
