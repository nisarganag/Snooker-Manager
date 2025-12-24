package com.alertix.snooker.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.Frame;
import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchEvent;
import com.alertix.snooker.domain.MatchEventType;
import com.alertix.snooker.domain.MatchStatus;
import com.alertix.snooker.domain.Player;
import com.alertix.snooker.domain.Tournament;
import com.alertix.snooker.domain.User;
import com.alertix.snooker.payload.request.EventRequest;
import com.alertix.snooker.payload.request.MatchRequest;
import com.alertix.snooker.repository.FrameRepository;
import com.alertix.snooker.repository.MatchEventRepository;
import com.alertix.snooker.repository.MatchRepository;
import com.alertix.snooker.repository.PlayerRepository;
import com.alertix.snooker.repository.TournamentRepository;
import com.alertix.snooker.repository.UserRepository;
import com.alertix.snooker.service.EmailService;
import com.alertix.snooker.service.MatchService;
import com.alertix.snooker.service.NotificationService;
import com.alertix.snooker.utils.EmailTemplateUtils;

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

    @Autowired
    private MatchEventRepository matchEventRepository;

    @Autowired
    private FrameRepository frameRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

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
        Match savedMatch = saveOrUpdateMatchFromRequest(match, request);
        notificationService.sendMatchScheduleNotification(savedMatch);
        return savedMatch;
    }

    @Override
    public Match updateMatch(Long id, MatchRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Match request cannot be null");
        }
        return matchRepository.findById(id).map(m -> {
            boolean timeChanged = request.getStartTime() != null && !request.getStartTime().equals(m.getStartTime());
            Match savedMatch = saveOrUpdateMatchFromRequest(m, request);
            if (timeChanged) {
                notificationService.sendMatchRescheduleNotification(savedMatch);
            }
            return savedMatch;
        }).orElseThrow(() -> new RuntimeException("Match not found"));
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
    public List<Match> getMatchesByStatus(MatchStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Match status cannot be null");
        }
        return matchRepository.findByStatus(status);
    }

    @Override
    public Match updateMatchStatus(Long id, MatchStatus status) {
        if (id == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        Match m = matchRepository.findById(id).orElseThrow(() -> new RuntimeException("Match not found"));
        m.setStatus(status);

        if (status == MatchStatus.COMPLETED) {
            // Calculate scores (Frames won)
            List<Frame> frames = frameRepository.findByMatchId(m.getId());
            int p1Frames = 0;
            int p2Frames = 0;
            for (Frame f : frames) {
                if (f.getPointsPlayer1() > f.getPointsPlayer2())
                    p1Frames++;
                else if (f.getPointsPlayer2() > f.getPointsPlayer1())
                    p2Frames++;
            }

            String winner = p1Frames > p2Frames ? m.getPlayer1().getName()
                    : (p2Frames > p1Frames ? m.getPlayer2().getName() : "Draw");

            String subject = "Match Result: " + m.getPlayer1().getName() + " vs " + m.getPlayer2().getName();
            String content = EmailTemplateUtils.getMatchResultEmail(
                    m.getTournament() != null ? m.getTournament().getName() : "Friendly Match",
                    m.getPlayer1().getName(),
                    m.getPlayer2().getName(),
                    p1Frames,
                    p2Frames,
                    winner);
            emailService.broadcastMatchResult(subject, content);
        }

        return matchRepository.save(m);
    }

    @Override
    public void deleteMatch(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        matchRepository.deleteById(id);
    }

    @Override
    public MatchEvent recordEvent(EventRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Event request cannot be null");

        Long frameId = request.getFrameId();
        if (frameId == null) {
            throw new IllegalArgumentException("Frame ID cannot be null");
        }
        Frame frame = frameRepository.findById(frameId)
                .orElseThrow(() -> new RuntimeException("Frame not found"));

        Player player = null;
        Long playerId = request.getPlayerId();
        if (playerId != null) {
            player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new RuntimeException("Player not found"));
        }

        MatchEvent event = new MatchEvent(frame, player, request.getType(), request.getPoints(), request.getDetails());
        matchEventRepository.save(event);

        boolean isPlayer1 = player != null && player.getId().equals(frame.getMatch().getPlayer1().getId());

        if (request.getType() == MatchEventType.FOUL) {
            if (isPlayer1) {
                frame.setPointsPlayer2(frame.getPointsPlayer2() + request.getPoints());
            } else {
                frame.setPointsPlayer1(frame.getPointsPlayer1() + request.getPoints());
            }
        } else {
            if (isPlayer1) {
                frame.setPointsPlayer1(frame.getPointsPlayer1() + request.getPoints());
            } else {
                frame.setPointsPlayer2(frame.getPointsPlayer2() + request.getPoints());
            }
        }

        frameRepository.save(frame);
        return event;
    }

    @Override
    public void undoLastEvent(Long frameId) {
        List<MatchEvent> events = matchEventRepository.findByFrameIdOrderByTimestampDesc(frameId);
        if (events.isEmpty())
            return;

        MatchEvent lastEvent = events.get(0);
        Frame frame = lastEvent.getFrame();
        Player player = lastEvent.getPlayer();

        boolean isPlayer1 = player != null && player.getId().equals(frame.getMatch().getPlayer1().getId());

        if (lastEvent.getType() == MatchEventType.FOUL) {
            if (isPlayer1) {
                frame.setPointsPlayer2(frame.getPointsPlayer2() - lastEvent.getPoints());
            } else {
                frame.setPointsPlayer1(frame.getPointsPlayer1() - lastEvent.getPoints());
            }
        } else {
            if (isPlayer1) {
                frame.setPointsPlayer1(frame.getPointsPlayer1() - lastEvent.getPoints());
            } else {
                frame.setPointsPlayer2(frame.getPointsPlayer2() - lastEvent.getPoints());
            }
        }

        matchEventRepository.delete(lastEvent);
        frameRepository.save(frame);
    }
}
