package com.alertix.snooker.utils;

public class EmailTemplateUtils {

    private static final String HEADER_COLOR = "#1a1a2e";
    private static final String ACCENT_COLOR = "#e94560";
    private static final String TEXT_COLOR = "#333333";
    private static final String BG_COLOR = "#f4f4f4";

    private static String getBaseTemplate(String title, String bodyContent) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='utf-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: "
                + BG_COLOR + "; }" +
                ".container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }"
                +
                ".header { background-color: " + HEADER_COLOR + "; padding: 30px; text-align: center; }" +
                ".header h1 { color: #ffffff; margin: 0; font-size: 24px; letter-spacing: 1px; }" +
                ".content { padding: 40px 30px; color: " + TEXT_COLOR + "; line-height: 1.6; }" +
                ".button { display: inline-block; padding: 12px 24px; background-color: " + ACCENT_COLOR
                + "; color: #ffffff; text-decoration: none; border-radius: 4px; font-weight: bold; margin-top: 20px; }"
                +
                ".footer { background-color: #eeeeee; padding: 20px; text-align: center; font-size: 12px; color: #777777; }"
                +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>" + title + "</h1>" +
                "</div>" +
                "<div class='content'>" +
                bodyContent +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2023 Alertix Snooker. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    public static String getWelcomeEmail(String username) {
        String body = "<h2 style='color: " + HEADER_COLOR + "; margin-top: 0;'>Welcome, " + username + "!</h2>" +
                "<p>Thank you for joining Alertix Snooker. We are thrilled to have you as part of our community.</p>" +
                "<p>Get ready to track matches, tournaments, and live scores like never before.</p>" +
                "<a href='#' class='button'>Go to Dashboard</a>"; // Link can be updated to actual FE URL
        return getBaseTemplate("Welcome Aboard!", body);
    }

    public static String getPasswordResetEmail(String link) {
        String body = "<h2 style='color: " + HEADER_COLOR + "; margin-top: 0;'>Reset Your Password</h2>" +
                "<p>We received a request to reset your password. If you didn't make this request, you can safely ignore this email.</p>"
                +
                "<p>To reset your password, click the button below:</p>" +
                "<a href='" + link + "' class='button'>Reset Password</a>" +
                "<p style='margin-top: 30px; font-size: 13px; color: #999;'>Link expires in 24 hours.</p>";
        return getBaseTemplate("Password Reset", body);
    }

    public static String getMatchResultEmail(String tournamentName, String p1Name, String p2Name, int score1,
            int score2, String winnerName) {
        String body = "<h2 style='color: " + HEADER_COLOR + "; margin-top: 0;'>Match Result</h2>" +
                "<p style='text-align: center; color: #777;'>" + tournamentName + "</p>" +
                "<div style='background: #f9f9f9; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0;'>"
                +
                "<div style='display: flex; justify-content: space-around; align-items: center;'>" +
                "<div style='flex: 1;'><h3>" + p1Name + "</h3></div>" +
                "<div style='background: " + ACCENT_COLOR
                + "; color: white; padding: 10px 20px; border-radius: 4px; font-weight: bold; font-size: 24px;'>" +
                score1 + " - " + score2 +
                "</div>" +
                "<div style='flex: 1;'><h3>" + p2Name + "</h3></div>" +
                "</div>" +
                "<p style='margin-top: 20px; font-weight: bold; color: " + ACCENT_COLOR + ";'>Winner: " + winnerName
                + "!</p>" +
                "</div>" +
                "<a href='#' class='button'>View Full Details</a>";
        return getBaseTemplate("New Match Result!", body);
    }
}
