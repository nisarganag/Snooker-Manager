package com.alertix.snooker.service;

import java.util.List;
import java.util.Optional;

import com.alertix.snooker.domain.Tournament;

public interface TournamentService {
    List<Tournament> getAllTournaments();

    Optional<Tournament> getTournamentById(Long id);

    Tournament createTournament(Tournament tournament);

    Tournament updateTournament(Long id, Tournament tournament);

    void deleteTournament(Long id);
}
