package com.alertix.snooker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchStatus;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournamentId(Long tournamentId);

    List<Match> findByStatus(MatchStatus status);
}
