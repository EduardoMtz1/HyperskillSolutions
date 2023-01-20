package server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class WebServer {
    private final int port;
    private HttpServer server;
    private BlockingDeque<String> accessCodeQueue = new LinkedBlockingDeque<>();
    public WebServer() {
        this.port = 8080;
        startServer();
    }
    private void startServer() {
        try{
            this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        HttpContext rootContext = this.server.createContext("/");
        rootContext.setHandler(this::handleRootTask);
        this.server.start();
    }
    public void stopServer() {
        this.server.stop(1);
    }
    private void handleRootTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if(query == null || !query.contains("code=")){
            sendResponse(exchange, "Authorization code not found. Try again.");
            return;
        }
        String code = query.split("code=")[1];
        accessCodeQueue.add(code);
        sendResponse(exchange, "Got the code. Return back to your program.");

    }
    public String getAccessCode() throws InterruptedException{
        return accessCodeQueue.take();
    }
    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}
