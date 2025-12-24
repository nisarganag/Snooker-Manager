package com.alertix.snooker.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchStatus;
import com.alertix.snooker.domain.Player;
import com.alertix.snooker.domain.Tournament;
import com.alertix.snooker.domain.User;
import com.alertix.snooker.payload.request.MatchRequest;
import com.alertix.snooker.repository.MatchRepository;
import com.alertix.snooker.repository.PlayerRepository;
import com.alertix.snooker.repository.TournamentRepository;
import com.alertix.snooker.repository.UserRepository;
import com.alertix.snooker.service.MatchService;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;

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
    public Match createMatch(MatchRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Match request cannot be null");
        }
        Match match = new Match();
        return saveOrUpdateMatchFromRequest(match, request);
    }

    @Override
    public Match updateMatch(Long id, MatchRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Match request cannot be null");
        }
        return matchRepository.findById(id).map(m -> saveOrUpdateMatchFromRequest(m, request))
                .orElseThrow(() -> new RuntimeException("Match not found"));
    }

    private Match saveOrUpdateMatchFromRequest(Match match, MatchRequest request) {
        Long p1Id = request.getPlayer1Id();
        if (p1Id != null) {
            Player p1 = playerRepository.findById(p1Id)
                    .orElseThrow(() -> new RuntimeException("Player 1 not found"));
            match.setPlayer1(p1);
        }
        Long p2Id = request.getPlayer2Id();
        if (p2Id != null) {
            Player p2 = playerRepository.findById(p2Id)
                    .orElseThrow(() -> new RuntimeException("Player 2 not found"));
            match.setPlayer2(p2);
        }
        Long refId = request.getRefereeId();
        if (refId != null) {
            User referee = userRepository.findById(refId)
                    .orElseThrow(() -> new RuntimeException("Referee not found"));
            match.setReferee(referee);
        }
        Long tId = request.getTournamentId();
        if (tId != null) {
            Tournament t = tournamentRepository.findById(tId)
                    .orElseThrow(() -> new RuntimeException("Tournament not found"));
            match.setTournament(t);
        }

        if (request.getStartTime() != null)
            match.setStartTime(request.getStartTime());
        match.setBestOfFrames(request.getBestOfFrames());

        if (match.getStatus() == null) {
            match.setStatus(MatchStatus.SCHEDULED);
        }

        return matchRepository.save(match);
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
