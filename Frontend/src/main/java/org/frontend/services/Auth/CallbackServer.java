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
                String code = null;
                boolean isLogin = false;

                // se esiste un parametro code è stato effettuato il login
                if (query != null) {
                    for (String param : query.split("&")) {
                        if (param.startsWith("code=")) {
                            code = URLDecoder.decode(param.substring(5), StandardCharsets.UTF_8);
                            isLogin = true;
                        }
                    }
                }

                String htmlResponse;

                if (isLogin) {
                    CognitoAuthService.exchangeCode(code);
                    htmlResponse = "<html><body style='text-align:center; font-family:sans-serif; paddingTop:50px;'>"
                            + "<h1>✅ Accesso Autorizzato!</h1>"
                            + "<p>Puoi chiudere questa scheda e tornare all'applicazione.</p>"
                            + "</body></html>";
                } else {
                    htmlResponse = "<html><body style='text-align:center; font-family:sans-serif; paddingTop:50px;'>"
                            + "<h1>👋 Disconnessione completata!</h1>"
                            + "<p>A presto. Puoi chiudere questa scheda.</p>"
                            + "</body></html>";
                }

                // invio risposta HTML al browser
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                byte[] responseBytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }

                // server spento
                stop();
            });

            server.setExecutor(null);
            server.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}