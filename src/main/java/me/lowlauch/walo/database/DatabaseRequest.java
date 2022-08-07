package me.lowlauch.walo.database;

import me.lowlauch.walo.WaloConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DatabaseRequest  {
    private static final String databaseAPIUrl = WaloConfig.getDatabaseAPIUrl();

    public static String request(String arguments) throws IOException {
        URL url = new URL(databaseAPIUrl + "?" + arguments);

        URLConnection urlConnection = url.openConnection();

        BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        urlConnection.getInputStream()));
        String inputLine;

        String returnString = "";
        while ((inputLine = input.readLine()) != null) {
            returnString += inputLine;
        }

        input.close();

        return returnString;
    }
}
