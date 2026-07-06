package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StaticHandler implements HttpHandler {

    private final String htmlContent;

    public StaticHandler(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] bytes = htmlContent.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
