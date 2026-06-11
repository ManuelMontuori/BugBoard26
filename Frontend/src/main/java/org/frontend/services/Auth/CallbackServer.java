package org.frontend.services.Auth;

import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CallbackServer {

    private HttpServer server;

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(9090), 0);

            server.createContext("/callback", exchange -> {

                String query = exchange.getRequestURI().getQuery();

                System.out.println("CALLBACK RICEVUTO: " + query);

                String code = null;

                if (query != null) {
                    for (String param : query.split("&")) {
                        if (param.startsWith("code=")) {
                            code = URLDecoder.decode(param.substring(5), StandardCharsets.UTF_8);
                        }
                    }
                }

                System.out.println("CODE PARSED = " + code);

                CognitoAuthService.exchangeCode(code);

                String response = "Login completato. Puoi chiudere questa finestra.";
                exchange.sendResponseHeaders(200, response.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });

            server.setExecutor(null);
            server.start();

            System.out.println("Server avviato su http://localhost:9090");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("CallbackServer fermato.");
        }
    }
}