package com.example.telegramlogger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TelegramNotifier {
    private final String botToken;
    private final String chatId;

    public TelegramNotifier(String botToken, String chatId) {
        this.botToken = botToken;
        this.chatId = chatId;
    }

    public void sendMessage(String message) {
        try {
            String urlString = "https://api.telegram.org/bot" + botToken + "/sendMessage";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String payload = "chat_id=" + URLEncoder.encode(chatId, "UTF-8") +
                             "&text=" + URLEncoder.encode(message, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
            }

            conn.getInputStream().close(); // ensure response is read
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
