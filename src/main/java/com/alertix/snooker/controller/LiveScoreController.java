package com.alertix.snooker.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.alertix.snooker.payload.ScoreUpdate;

@Controller
public class LiveScoreController {

    @MessageMapping("/score/{matchId}")
    @SendTo("/topic/match/{matchId}")
    public ScoreUpdate updateScore(@DestinationVariable Long matchId, ScoreUpdate scoreUpdate) {
        // Logic to update DB would go here (call MatchService/ScoreService)
        // For now, we simply broadcast the update to subscribers
        return scoreUpdate;
    }
}
