package com.alertix.snooker.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchStatus;
import com.alertix.snooker.repository.MatchRepository;
import com.alertix.snooker.service.MatchService;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getMatchesByTournament(Long tournamentId) {
        if (tournamentId == null) {
            throw new IllegalArgumentException("Tournament ID cannot be null");
        }
        return matchRepository.findByTournamentId(tournamentId);
    }

    @Override
    public Optional<Match> getMatchById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return matchRepository.findById(id);
    }

    @Override
    public Match createMatch(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }
        if (match.getStatus() == null) {
            match.setStatus(MatchStatus.SCHEDULED);
        }
        return matchRepository.save(match);
    }

    @Override
    public Match updateMatch(Long id, Match details) {
        if (id == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        if (details == null) {
            throw new IllegalArgumentException("Match details cannot be null");
        }
        return matchRepository.findById(id).map(m -> {
            m.setPlayer1(details.getPlayer1());
            m.setPlayer2(details.getPlayer2());
            m.setStartTime(details.getStartTime());
            m.setBestOfFrames(details.getBestOfFrames());
            m.setTournament(details.getTournament());
            return matchRepository.save(m);
        }).orElseThrow(() -> new RuntimeException("Match not found"));
    }

    @Override
    public Match updateMatchStatus(Long id, MatchStatus status) {
        if (id == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        Match m = matchRepository.findById(id).orElseThrow(() -> new RuntimeException("Match not found"));
        m.setStatus(status);
        return matchRepository.save(m);
    }

    @Override
    public void deleteMatch(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        matchRepository.deleteById(id);
    }
}
