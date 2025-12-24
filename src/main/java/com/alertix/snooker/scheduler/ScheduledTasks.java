package com.alertix.snooker.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alertix.snooker.domain.Match;
import com.alertix.snooker.domain.MatchStatus;
import com.alertix.snooker.repository.MatchRepository;
import com.alertix.snooker.service.EmailService;
import com.alertix.snooker.utils.EmailTemplateUtils;

@Component
public class ScheduledTasks {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EmailService emailService;

    // Run at 8:00 AM every day
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyRecap() {
        // Get matches from yesterday
        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();

        List<Match> completedMatches = matchRepository.findByStatus(MatchStatus.COMPLETED).stream()
                .filter(m -> m.getStartTime() != null && m.getStartTime().isAfter(start)
                        && m.getStartTime().isBefore(end)) // Approximation if startTime is close to finish time
                // Better would be to have 'endTime' or 'updatedAt' field.
                // For now, let's just list matches scheduled for yesterday or completed.
                // Let's assume we want to show results of matches that *started* yesterday.
                .collect(Collectors.toList());

        if (completedMatches.isEmpty()) {
            return;
        }

        StringBuilder content = new StringBuilder();
        content.append("<ul>");
        for (Match m : completedMatches) {
            String result = m.getPlayer1().getName() + " vs " + m.getPlayer2().getName() +
                    " (" + m.getScorePlayer1() + "-" + m.getScorePlayer2() + ")";
            content.append("<li>").append(result).append("</li>");
        }
        content.append("</ul>");

        // Broadcast to newsletter subscribers
        String htmlContent = EmailTemplateUtils.getDailyRecapEmail(content.toString());
        emailService.broadcastMatchResult("Daily Snooker Recap", htmlContent);
        // Reusing broadcastMatchResult which sends to all active subscribers
    }
}
