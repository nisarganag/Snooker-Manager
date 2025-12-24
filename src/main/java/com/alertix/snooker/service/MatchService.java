package com.alertix.snooker.service;

import java.util.List;
import java.util.Optional;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchStatus;
import com.alertix.snooker.payload.request.MatchRequest;

public interface MatchService {
    List<Match> getAllMatches();

    List<Match> getMatchesByTournament(Long tournamentId);

    Optional<Match> getMatchById(Long id);

    Match createMatch(MatchRequest request);

    Match updateMatch(Long id, MatchRequest request);

    Match updateMatchStatus(Long id, MatchStatus status);

    void deleteMatch(Long id);
}
