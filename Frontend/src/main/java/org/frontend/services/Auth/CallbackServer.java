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

                // 1. Controlliamo se c'è un parametro 'code' (Significa che è un LOGIN)
                if (query != null) {
                    for (String param : query.split("&")) {
                        if (param.startsWith("code=")) {
                            code = URLDecoder.decode(param.substring(5), StandardCharsets.UTF_8);
                            isLogin = true;
                        }
                    }
                }

                String htmlResponse;

                // 2. Smistiamo la logica tra Login e Logout
                if (isLogin) {
                    // FLUSSO DI LOGIN
                    CognitoAuthService.exchangeCode(code);
                    htmlResponse = "<html><body style='text-align:center; font-family:sans-serif; paddingTop:50px;'>"
                            + "<h1>✅ Accesso Autorizzato!</h1>"
                            + "<p>Puoi chiudere questa scheda e tornare all'applicazione.</p>"
                            + "</body></html>";
                } else {
                    // FLUSSO DI LOGOUT (Nessun 'code' trovato nell'URL)
                    htmlResponse = "<html><body style='text-align:center; font-family:sans-serif; paddingTop:50px;'>"
                            + "<h1>👋 Disconnessione completata!</h1>"
                            + "<p>A presto. Puoi chiudere questa scheda.</p>"
                            + "</body></html>";
                }

                // 3. Inviamo la risposta HTML al browser
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                byte[] responseBytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }

                // 4. Spegniamo il server per liberare la porta
                stop();
            });

            server.setExecutor(null);
            server.start();
            System.out.println("CallbackServer in ascolto su http://localhost:9090");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("CallbackServer fermato e porta liberata.");
        }
    }
}