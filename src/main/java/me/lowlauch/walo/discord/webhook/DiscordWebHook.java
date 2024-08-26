package me.lowlauch.walo.discord.webhook;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class DiscordWebHook {
    static String webHookURL = WaloConfig.getDiscordWebhookURL();

    public static void sendText(String text) {
        if (webHookURL.isEmpty())
            return;

        if (!webHookURL.contains("https://")) {
            Bukkit.getLogger().info(Main.prefix + "Discord Webhook URL invalid!");
            return;
        }

        URL url = null;

        try {
            url = new URL(webHookURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 "); // Without this, we get 403 connection refused

        connection.setDoOutput(true);

        String webRequestJSON = "{\"content\":\"" + text + "\", \"allowed_mentions\": { \"parse\": [\"everyone\"] }}";

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = webRequestJSON.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}