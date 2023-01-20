package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MusicAdvisorClient {
    private String resourceLink;
    private String token;
    private HttpClient client;
    private Map<String, String> categoriesId = new LinkedHashMap<>();
    public MusicAdvisorClient(String resourceLink) {
        this.resourceLink = resourceLink;
        this.client =  HttpClient.newBuilder().build();
    }

    public void setToken(String token) {
        this.token = token;
    }
    private HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + this.token)
                .uri(URI.create(resourceLink + path));

    }

    private JsonObject makeRequest(String path) throws IOException, InterruptedException {
        HttpRequest request = requestBuilder(path)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }

    void catchCategories() {
        try {
            JsonObject responseBody = makeRequest("/v1/browse/categories");
            if(responseBody.has("error")){
                System.out.println(responseBody.getAsJsonObject("error").get("message").getAsString());
                return;
            }

            List<JsonObject> categories = new ArrayList<>();
            responseBody.getAsJsonObject("categories")
                    .getAsJsonArray("items")
                    .forEach(category -> categories.add(category.getAsJsonObject()));

            this.categoriesId.clear();
            for(JsonObject category: categories) {
                String name = category.get("name").getAsString();
                String id = category.get("id").getAsString();
                categoriesId.put(name.trim(), id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    List<String> getCategories() {
        catchCategories();
        List<String> result = new ArrayList<String>();
        for(String category: categoriesId.keySet()) {
            result.add(category);
        }
        return result;
    }

    List<String> getNewReleases() {
        try {
            JsonObject responseBody = makeRequest("/v1/browse/new-releases");
            if(responseBody.has("error")){
                System.out.println(responseBody.getAsJsonObject("error").get("message").getAsString());
                return null;
            }
            List<String> result = new ArrayList<String>();
            List<JsonObject> albums = new ArrayList<>();
            responseBody.getAsJsonObject("albums")
                    .getAsJsonArray("items")
                    .forEach(album -> albums.add(album.getAsJsonObject()));
            for(JsonObject album: albums) {
                String name = album.get("name").getAsString();
                String url = album.getAsJsonObject("external_urls").get("spotify").getAsString();
                List<JsonObject> artistsList = new ArrayList<>();
                album.getAsJsonArray("artists")
                        .forEach(artist -> artistsList.add(artist.getAsJsonObject()));
                String artists = "[";
                for(int i = 0; i < artistsList.size(); i++){
                    String artistName = artistsList.get(i).get("name").getAsString().trim();
                    artists = artists + artistName;
                    if(i < artistsList.size() -1) {
                        artists = artists + ", ";
                    }
                }
                artists = artists + "]";
                /*System.out.println(name);
                System.out.println(artists);
                System.out.println(url);
                System.out.println();*/
                result.add(name + "\n" + artists + "\n" + url + "\n");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    List<String> getParsedPlaylists(JsonObject responseBody) {
        if(responseBody.has("error")){
            System.out.println(responseBody.getAsJsonObject("error").get("message").getAsString());
            return null;
        }

        List<String> result = new ArrayList<String>();
        List<JsonObject> playlists = new ArrayList<>();
        responseBody.getAsJsonObject("playlists")
                .getAsJsonArray("items")
                .forEach(playlist -> playlists.add(playlist.getAsJsonObject()));
        for(JsonObject playlist: playlists) {
            String name = playlist.get("name").getAsString();
            String url = playlist.getAsJsonObject("external_urls").get("spotify").getAsString();
            /*System.out.println(name);
            System.out.println(url);
            System.out.println();*/
            result.add(name + "\n" + url + "\n");
        }
        return result;
    }
     List<String> getFeaturedPlaylists() {
         try {
             JsonObject responseBody = makeRequest("/v1/browse/featured-playlists");
             return getParsedPlaylists(responseBody);
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }

     List<String>  getPlaylists(String category) {
        if(this.categoriesId.isEmpty()){
            catchCategories();
        }
        String categoryId = categoriesId.get(category.trim());
        if(categoryId == null) {
            System.out.println("Unknown category name.");
            return null;
        }

         try {
             JsonObject responseBody = makeRequest("/v1/browse/categories/" + categoryId + "/playlists");
             return getParsedPlaylists(responseBody);
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }
}
