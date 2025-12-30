package com.alertix.snooker.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alertix.snooker.payload.response.YouTubeLiveStreamResponse;
import com.alertix.snooker.service.YouTubeService;
import com.alertix.snooker.utils.TitleParser;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

@Service
public class YouTubeServiceImpl implements YouTubeService {

    private static final Logger logger = LoggerFactory.getLogger(YouTubeServiceImpl.class);

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.channel.id}")
    private String channelId;

    private static final String APPLICATION_NAME = "Snooker-Tournament-Manager";

    @Override
    @Cacheable(value = "youtubeStreams", unless = "#result == null || #result.isEmpty()")
    public List<YouTubeLiveStreamResponse> getLiveStreams() {
        try {
            YouTube youtube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // First, try to get live broadcasts
            List<YouTubeLiveStreamResponse> liveStreams = fetchLiveStreams(youtube);

            // If no live streams, fallback to most recent completed streams
            if (liveStreams.isEmpty()) {
                logger.info("No live streams found, fetching recent completed streams");
                return fetchRecentCompletedStreams(youtube);
            }

            return liveStreams;

        } catch (@SuppressWarnings("unused") Exception e) {
            logger.error("Error fetching YouTube live streams", e);
            return new ArrayList<>();
        }
    }

    private List<YouTubeLiveStreamResponse> fetchLiveStreams(YouTube youtube) throws Exception {
        // Search for live broadcasts on the channel
        YouTube.Search.List search = youtube.search()
                .list(Arrays.asList("id", "snippet"))
                .setKey(apiKey)
                .setChannelId(channelId)
                .setEventType("live")
                .setType(Arrays.asList("video"))
                .setMaxResults(10L);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResults = searchResponse.getItems();

        if (searchResults == null || searchResults.isEmpty()) {
            return new ArrayList<>();
        }

        return fetchVideoDetails(youtube, searchResults, true);
    }

    private List<YouTubeLiveStreamResponse> fetchRecentCompletedStreams(YouTube youtube) throws Exception {
        // Search for completed broadcasts from the channel
        YouTube.Search.List search = youtube.search()
                .list(Arrays.asList("id", "snippet"))
                .setKey(apiKey)
                .setChannelId(channelId)
                .setEventType("completed")
                .setType(Arrays.asList("video"))
                .setOrder("date")
                .setMaxResults(10L);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResults = searchResponse.getItems();

        if (searchResults == null || searchResults.isEmpty()) {
            return new ArrayList<>();
        }

        return fetchVideoDetails(youtube, searchResults, false);
    }

    private List<YouTubeLiveStreamResponse> fetchVideoDetails(YouTube youtube, List<SearchResult> searchResults,
            boolean isLive) throws Exception {
        // Get video IDs
        String videoIds = searchResults.stream()
                .map(result -> result.getId().getVideoId())
                .collect(Collectors.joining(","));

        // Fetch detailed video information including live streaming details
        YouTube.Videos.List videoRequest = youtube.videos()
                .list(Arrays.asList("id", "snippet", "liveStreamingDetails", "statistics"))
                .setKey(apiKey)
                .setId(Arrays.asList(videoIds));

        VideoListResponse videoResponse = videoRequest.execute();
        List<Video> videos = videoResponse.getItems();

        // Transform to our response format
        return videos.stream()
                .map(video -> transformToResponse(video, isLive))
                .collect(Collectors.toList());
    }

    private YouTubeLiveStreamResponse transformToResponse(Video video, boolean isLive) {
        YouTubeLiveStreamResponse response = new YouTubeLiveStreamResponse();

        // Basic info
        response.setId(video.getId());
        response.setYoutubeStreamId(video.getId());
        response.setTitle(video.getSnippet().getTitle());
        response.setDescription(video.getSnippet().getDescription());
        response.setChannelTitle(video.getSnippet().getChannelTitle());
        response.setIsLive(isLive);

        // Thumbnail (prefer high quality)
        if (video.getSnippet().getThumbnails() != null) {
            if (video.getSnippet().getThumbnails().getHigh() != null) {
                response.setThumbnail(video.getSnippet().getThumbnails().getHigh().getUrl());
            } else if (video.getSnippet().getThumbnails().getMedium() != null) {
                response.setThumbnail(video.getSnippet().getThumbnails().getMedium().getUrl());
            } else if (video.getSnippet().getThumbnails().getDefault() != null) {
                response.setThumbnail(video.getSnippet().getThumbnails().getDefault().getUrl());
            }
        }

        // Live streaming details
        if (video.getLiveStreamingDetails() != null) {
            if (video.getLiveStreamingDetails().getActualStartTime() != null) {
                response.setStartTime(video.getLiveStreamingDetails().getActualStartTime().toString());
            } else if (video.getLiveStreamingDetails().getScheduledStartTime() != null) {
                response.setStartTime(video.getLiveStreamingDetails().getScheduledStartTime().toString());
            }

            if (video.getLiveStreamingDetails().getConcurrentViewers() != null) {
                response.setViewerCount(video.getLiveStreamingDetails().getConcurrentViewers().longValue());
            } else {
                response.setViewerCount(0L);
            }
        }

        // Parse player names from title
        TitleParser.ParsedPlayers parsedPlayers = TitleParser.parsePlayerNames(video.getSnippet().getTitle());

        if (parsedPlayers.isSuccess()) {
            response.setPlayer1(parsedPlayers.getPlayer1());
            response.setPlayer2(parsedPlayers.getPlayer2());
            response.setParsedTitle(true);
        } else {
            response.setParsedTitle(false);
            response.setOriginalTitle(video.getSnippet().getTitle());
        }

        return response;
    }
}
