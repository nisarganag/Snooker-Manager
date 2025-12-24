package com.alertix.snooker.service;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.Player;
import com.alertix.snooker.domain.Tournament;
import com.alertix.snooker.domain.User;
import com.alertix.snooker.utils.EmailTemplateUtils;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    // Use a standard formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm");

    @Async
    public void sendTournamentRegistrationConfirmation(User user, Tournament tournament) {
        if (user.getEmail() == null)
            return;

        String startDate = tournament.getStartDate() != null ? tournament.getStartDate().format(DATE_FORMATTER) : "TBA";
        String content = EmailTemplateUtils.getTournamentRegistrationEmail(tournament.getName(), startDate);
        emailService.sendInfoEmail(user.getEmail(), "Registration Confirmed: " + tournament.getName(), content);
    }

    @Async
    public void sendMatchScheduleNotification(Match match) {
        notifyPlayerOfMatch(match.getPlayer1(), match.getPlayer2(), match, false);
        notifyPlayerOfMatch(match.getPlayer2(), match.getPlayer1(), match, false);
    }

    @Async
    public void sendMatchRescheduleNotification(Match match) {
        notifyPlayerOfMatch(match.getPlayer1(), match.getPlayer2(), match, true);
        notifyPlayerOfMatch(match.getPlayer2(), match.getPlayer1(), match, true);
    }

    private void notifyPlayerOfMatch(Player player, Player opponent, Match match, boolean isReschedule) {
        if (player == null || player.getUser() == null || player.getUser().getEmail() == null) {
            return;
        }

        String tournamentName = match.getTournament() != null ? match.getTournament().getName() : "Friendly Match";
        String opponentName = opponent != null ? opponent.getName() : "TBD";
        String time = match.getStartTime() != null ? match.getStartTime().format(TIME_FORMATTER) : "TBA";

        if (isReschedule) {
            String content = EmailTemplateUtils.getMatchRescheduleEmail(tournamentName, opponentName, time);
            emailService.sendInfoEmail(player.getUser().getEmail(), "Match Rescheduled: vs " + opponentName, content);
        } else {
            String content = EmailTemplateUtils.getMatchScheduleEmail(tournamentName, opponentName, time);
            emailService.sendInfoEmail(player.getUser().getEmail(), "Match Scheduled: vs " + opponentName, content);
        }
    }
}
