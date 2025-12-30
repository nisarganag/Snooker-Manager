package com.alertix.snooker.controller;

import com.alertix.snooker.payload.response.YouTubeLiveStreamResponse;
import com.alertix.snooker.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/youtube")
public class YouTubeController {

    @Autowired
    private YouTubeService youtubeService;

    /**
     * Fetch all currently live streams from the UPBSA YouTube channel
     * 
     * @return List of live stream objects
     */
    @GetMapping("/live-streams")
    public ResponseEntity<List<YouTubeLiveStreamResponse>> getLiveStreams() {
        List<YouTubeLiveStreamResponse> liveStreams = youtubeService.getLiveStreams();
        return ResponseEntity.ok(liveStreams);
    }
}
