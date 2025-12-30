package com.alertix.snooker.service;

import com.alertix.snooker.payload.response.YouTubeLiveStreamResponse;
import java.util.List;

public interface YouTubeService {
    /**
     * Fetch currently live streams from the configured YouTube channel
     * 
     * @return List of live stream responses
     */
    List<YouTubeLiveStreamResponse> getLiveStreams();
}
