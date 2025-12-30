package com.alertix.snooker.utils;

import com.alertix.snooker.payload.response.PlayerInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleParser {

    // Regex patterns to match player names in various formats
    private static final Pattern[] PATTERNS = {
            // "Player1 vs Player2", "Player1 VS Player2", "Player1 v/s Player2"
            Pattern.compile("^(.+?)\\s+(?:vs|VS|v/s|V/S)\\s+(.+?)(?:\\s*[|\\-–]|$)", Pattern.CASE_INSENSITIVE),
            // "Player1 - Player2"
            Pattern.compile("^(.+?)\\s*[-–]\\s*(.+?)(?:\\s*[|\\-–]|$)"),
    };

    public static class ParsedPlayers {
        private final PlayerInfo player1;
        private final PlayerInfo player2;
        private final boolean success;

        public ParsedPlayers(PlayerInfo player1, PlayerInfo player2, boolean success) {
            this.player1 = player1;
            this.player2 = player2;
            this.success = success;
        }

        public PlayerInfo getPlayer1() {
            return player1;
        }

        public PlayerInfo getPlayer2() {
            return player2;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    /**
     * Parse player names from video title
     * 
     * @param title The video title
     * @return ParsedPlayers object containing player info and success status
     */
    public static ParsedPlayers parsePlayerNames(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ParsedPlayers(null, null, false);
        }

        for (Pattern pattern : PATTERNS) {
            Matcher matcher = pattern.matcher(title.trim());
            if (matcher.find()) {
                String player1Name = matcher.group(1).trim();
                String player2Name = matcher.group(2).trim();

                // Validate that we actually got player names
                if (!player1Name.isEmpty() && !player2Name.isEmpty()) {
                    PlayerInfo player1 = new PlayerInfo(player1Name);
                    PlayerInfo player2 = new PlayerInfo(player2Name);
                    return new ParsedPlayers(player1, player2, true);
                }
            }
        }

        return new ParsedPlayers(null, null, false);
    }
}
