package com.cicd.automation.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class WebhookSignatureValidator {

    private static final Logger logger = LoggerFactory.getLogger(WebhookSignatureValidator.class);
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SHA256_PREFIX = "sha256=";

    public boolean validateGitHubSignature(String payload, String signature, String secret) {
        if (signature == null || !signature.startsWith(SHA256_PREFIX)) {
            return false;
        }

        String expectedSignature = generateSignature(payload, secret);
        String receivedSignature = signature.substring(SHA256_PREFIX.length());

        return safeEquals(expectedSignature, receivedSignature);
    }

    private String generateSignature(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error generating signature", e);
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private boolean safeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }

        return result == 0;
    }
}
