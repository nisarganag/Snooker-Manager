package com.alertix.snooker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchEvent;
import com.alertix.snooker.payload.request.EventRequest;
import com.alertix.snooker.payload.request.IdRequest;
import com.alertix.snooker.payload.request.MatchRequest;
import com.alertix.snooker.payload.request.MatchStatusRequest;
import com.alertix.snooker.service.MatchService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/all")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @PostMapping("/tournament")
    public List<Match> getMatchesByTournament(@RequestBody IdRequest request) {
        return matchService.getMatchesByTournament(request.getId());
    }

    @PostMapping("/get")
    public ResponseEntity<Match> getMatchById(@RequestBody IdRequest request) {
        return matchService.getMatchById(request.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Match createMatch(@RequestBody MatchRequest request) {
        return matchService.createMatch(request);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFEREE')")
    public ResponseEntity<Match> updateMatch(@RequestBody UpdateMatchRequest request) {
        try {
            return ResponseEntity.ok(matchService.updateMatch(request.getId(), request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update DTO needs ID
    public static class UpdateMatchRequest extends MatchRequest {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @PostMapping("/filter")
    public List<Match> getMatchesByStatus(@RequestBody MatchStatusRequest request) {
        return matchService.getMatchesByStatus(request.getStatus());
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFEREE')")
    public ResponseEntity<Match> updateMatchStatus(@RequestBody MatchStatusRequest request) {
        try {
            return ResponseEntity.ok(matchService.updateMatchStatus(request.getMatchId(), request.getStatus()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/event")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFEREE')")
    public ResponseEntity<MatchEvent> recordEvent(@RequestBody EventRequest request) {
        return ResponseEntity.ok(matchService.recordEvent(request));
    }

    @PostMapping("/undo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFEREE')")
    public ResponseEntity<?> undoLastEvent(@RequestBody IdRequest request) {
        // ID in request is frameId
        matchService.undoLastEvent(request.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMatch(@RequestBody IdRequest request) {
        matchService.deleteMatch(request.getId());
        return ResponseEntity.ok().build();
    }
}
