package com.alertix.snooker.service;

import java.util.List;
import java.util.Optional;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchStatus;

public interface MatchService {
    List<Match> getAllMatches();

    List<Match> getMatchesByTournament(Long tournamentId);

    Optional<Match> getMatchById(Long id);

    Match createMatch(Match match);

    Match updateMatch(Long id, Match match);

    Match updateMatchStatus(Long id, MatchStatus status);

    void deleteMatch(Long id);
}
