package com.alertix.snooker.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.Tournament;
import com.alertix.snooker.repository.TournamentRepository;
import com.alertix.snooker.service.TournamentService;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    public Optional<Tournament> getTournamentById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return tournamentRepository.findById(id);
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        // Default status if null
        if (tournament.getStatus() == null) {
            tournament.setStatus("UPCOMING");
        }
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament updateTournament(Long id, Tournament details) {
        if (id == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }
        return tournamentRepository.findById(id).map(t -> {
            t.setName(details.getName());
            t.setStartDate(details.getStartDate());
            t.setEndDate(details.getEndDate());
            t.setStatus(details.getStatus());
            return tournamentRepository.save(t);
        }).orElseThrow(() -> new RuntimeException("Tournament not found"));
    }

    @Override
    public void deleteTournament(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }
        tournamentRepository.deleteById(id);
    }
}
