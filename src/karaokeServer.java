import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class karaokeServer {
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new StaticFileHandler());
        server.setExecutor(null);
        server.start();
        
        System.out.println("Сервер запущен: http://localhost:8080");
        System.out.println("Нажмите Ctrl+C для остановки");
    }
    
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String filePath = path.equals("/") ? "web/index.html" : "web" + path;
            File file = new File(filePath);
            
            if (file.exists() && !file.isDirectory()) {
                String mime = filePath.endsWith(".html") ? "text/html" :
                              filePath.endsWith(".css") ? "text/css" : "text/plain";
                exchange.getResponseHeaders().set("Content-Type", mime);
                exchange.sendResponseHeaders(200, file.length());
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
            } else {
                String response = "404 - Файл не найден";
                exchange.sendResponseHeaders(404, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        }
    }
}
