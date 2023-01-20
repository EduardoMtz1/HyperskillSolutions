package advisor;
import client.Client;
import server.WebServer;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static boolean isAuth = false;
    private static int pageLength = 5;
    private static int actualPage = 0;
    private static List<String> contentToShow;

    private static void showResults() {
        if(contentToShow == null || contentToShow.size() == 0) {
            return;
        }
        if(pageLength * actualPage >= contentToShow.size() || actualPage < 0) {
            System.out.println("No more pages.");
            if(actualPage < 0) {
                actualPage = 0;
            } else {
                actualPage = (contentToShow.size() - 1) / pageLength;
            }
            return;
        }
        for(int i = pageLength * actualPage; i < (actualPage + 1) * pageLength; i++) {
            if( i < contentToShow.size()){
                System.out.println(contentToShow.get(i));
            }
        }
        System.out.println("---PAGE " + (actualPage + 1) + " OF " + ((contentToShow.size() - 1) / pageLength + 1) + "---");
    }
    public static void main(String[] args) throws IOException, InterruptedException{
        String redirectUri = "http://localhost:8080";
        String accessUri = "https://accounts.spotify.com";
        String apiUri = "https://api.spotify.com";
        String token = "";
        Scanner scanner = new Scanner(System.in);
        for(int i = 0; i < args.length; i++) {
            if(args[i].trim().equals("-access")){
                accessUri = args[i + 1].trim();
            }
            if(args[i].trim().equals("-resource")){
                apiUri = args[i + 1].trim();
            }
            if(args[i].trim().equals("-page")){
                pageLength = Math.max(Integer.parseInt(args[i + 1].trim()), 1);
            }
        }
        MusicAdvisorClient ma = new MusicAdvisorClient(apiUri);
        while(true) {
            String option = scanner.nextLine().trim();
            if(Main.isAuth){
                if(option.contains("playlists")){
                    String category = option.replace("playlists ", "");
                    contentToShow = ma.getPlaylists(category);
                    actualPage = 0;
                    showResults();
                } else {
                    switch (option) {
                        case "featured":
                            contentToShow = ma.getFeaturedPlaylists();
                            actualPage = 0;
                            showResults();
                            break;
                        case "new":
                            contentToShow = ma.getNewReleases();
                            actualPage = 0;
                            showResults();
                            break;
                        case "categories":
                            contentToShow = ma.getCategories();
                            actualPage = 0;
                            showResults();
                            break;
                        case "prev":
                            actualPage--;
                            showResults();
                            break;
                        case "next":
                            actualPage++;
                            showResults();
                            break;
                        case "exit":
                            if(contentToShow != null) {
                                contentToShow = null;
                                continue;
                            }
                            System.out.println("---GOODBYE!---");
                            System.exit(0);
                            return;
                        default:
                            break;
                    }
                }
            } else {
                if(option.equals("auth")){
                    WebServer server = new WebServer();
                    System.out.println("use this link to request the access code:");
                    System.out.println(accessUri + "/authorize?client_id=d766fe61c11940a786c736a0a7a70046&redirect_uri=" + redirectUri + "&response_type=code");
                    System.out.println("waiting for code...");
                    String code = server.getAccessCode();
                    System.out.println("code received");
                    System.out.println("making http request for access_token...");
                    Client client = new Client(accessUri);
                    token = client.requestToken(code);
                    ma.setToken(token);
                    Main.isAuth = true;
                    server.stopServer();
                    System.out.println("---SUCCESS---");
                } else if(option.equals("exit")) {
                    System.out.println("---GOODBYE!---");
                    System.exit(0);
                } else {
                    System.out.println("Please, provide access for application.");
                }
            }
        }
    }
}
