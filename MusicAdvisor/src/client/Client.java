package client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    private String tokenUri;
    private static String client_id = "d766fe61c11940a786c736a0a7a70046";
    private static String client_secret = "0e38b2beb4254f44ab978b2af0a456df";
    private static String redirect_uri = "http://localhost:8080";
    public Client(String accessLink) {
        tokenUri = accessLink + "/api/token";
    }
    public String requestToken(String code) throws IOException, InterruptedException {
        String body = "grant_type=authorization_code"
                + "&code=" + code
                + "&redirect_uri=" + redirect_uri
                + "&client_id=" + client_id
                + "&client_secret=" + client_secret;

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(tokenUri))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
        if(!responseBody.has("access_token")){
            throw new IllegalStateException("Not found access token: " + response.body());
        }

        return responseBody.get("access_token").getAsString();
    }
}
