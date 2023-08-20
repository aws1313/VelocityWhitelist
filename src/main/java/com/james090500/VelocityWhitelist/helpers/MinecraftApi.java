package com.james090500.VelocityWhitelist.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.james090500.VelocityWhitelist.VelocityWhitelist;
import com.james090500.VelocityWhitelist.config.Configs;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;



public record MinecraftApi(VelocityWhitelist velocityWhitelist) {

    /**
     * Returns player UUID
     *
     * @param username Username to get UUID from
     * @return Players uuid
     */
    public UUID getUUID(String username) {
        String prefix = Configs.getConfig().getGeyserPrefix();
        //Try get online player first
        if (velocityWhitelist.getServer().getPlayer(username).isPresent()) {
            return velocityWhitelist.getServer().getPlayer(username).get().getUniqueId();
        }
        else if (Configs.getConfig().isGeyserFloodgateEnabled()&&username.startsWith(prefix)){
            return getBedrockUUID(username, prefix);
        }

        else {
            JsonObject playerElement = getApiData(username);
            if (playerElement != null) {
                JsonElement playerUUID = playerElement.get("full_uuid");
                if (playerUUID != null && !playerUUID.isJsonNull()) {
                    return UUID.fromString(playerUUID.getAsString());
                }
            }
        }

        return null;
    }

    /**
     * Request API call for user data
     *
     * @param data The username/uuid to send
     * @return The response data
     */
    private static JsonObject getApiData(String data) {
        try {
            URL url = new URL("https://minecraftapi.net/api/v1/profile/" + data);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if (httpurlconnection.getResponseCode() / 100 == 2) {
                //Create reader
                BufferedReader in = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                //Read response
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);

                //Convert response to JSON
                return JsonParser.parseString(response.toString()).getAsJsonObject();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static UUID getBedrockUUID(String username, String prefix){
        Document doc;
        Map<String, String> cookies;
            try{
                Connection.Response response = Jsoup.connect("https://www.cxkes.me/xbox/xuid")
                        .method(Connection.Method.GET)
                        .execute();
                doc = response.parse();
                cookies=response.cookies();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Element input = doc.getElementsByTag("input").first();
            if (input!=null&&input.hasAttr("value")) {
                String token = input.attr("value");
                Document uuidDoc;
                System.out.println(cookies);
                Connection connection = Jsoup.connect("https://www.cxkes.me/xbox/xuid")
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .data("_token", token)
                        .cookies(cookies)
                        .data("gamertag", username.substring(prefix.length()));
                try{
                    uuidDoc=connection.post();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Element uuidElement = uuidDoc.getElementById("xuidHex");
                if (uuidElement!=null){
                    return UUID.fromString("00000000-0000-0000-"+new StringBuilder(uuidElement.text().trim()).insert(4,"-"));

                }
                else {
                    System.out.println("xuidHex not in response");
                }

            }
            else {
                System.out.println("Input or _token for Bedrock resolve missing");
            }
            return null;
    }

}