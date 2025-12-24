package com.alertix.snooker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alertix.snooker.domain.TournamentRegistration;

@Repository
public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistration, Long> {
    Optional<TournamentRegistration> findByTournamentIdAndUserId(Long tournamentId, Long userId);

    List<TournamentRegistration> findByTournamentId(Long tournamentId);

    List<TournamentRegistration> findByUserId(Long userId);
}
