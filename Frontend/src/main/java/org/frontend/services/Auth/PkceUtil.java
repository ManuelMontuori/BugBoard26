package org.frontend.services.Auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PkceUtil {
    private static final SecureRandom RNG = new SecureRandom();

    // RFC 7636: verifier 43-128 chars (URL-safe)
    public static String generateCodeVerifier() {
        byte[] bytes = new byte[32];
        RNG.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String codeChallengeS256(String codeVerifier) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("Unable to compute PKCE code_challenge", e);
        }
    }

    private PkceUtil() {}
}