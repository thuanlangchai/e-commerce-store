package com.minhthuan.web_store.domain.login_google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
@Component
public class GoogleTokenVerifier {

    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GoogleUserInfo verifyToken(String accessToken) {
        try {
            URL url = new URL(GOOGLE_USERINFO_URL + "?access_token=" + accessToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                log.error("Google API trả về lỗi: {}", responseCode);
                try (InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        String errorMessage = new String(errorStream.readAllBytes());
                        log.error("Error message: {}", errorMessage);
                    }
                }
                return null;
            }

            JsonNode jsonNode;
            try (InputStream inputStream = connection.getInputStream()) {
                jsonNode = objectMapper.readTree(inputStream);
            }

            String email = jsonNode.has("email") ? jsonNode.get("email").asText() : null;
            String name = jsonNode.has("name") ? jsonNode.get("name").asText() : null;
            String picture = jsonNode.has("picture") ? jsonNode.get("picture").asText() : null;
            String givenName = jsonNode.has("given_name") ? jsonNode.get("given_name").asText() : null;
            String familyName = jsonNode.has("family_name") ? jsonNode.get("family_name").asText() : null;
            boolean emailVerified = jsonNode.has("verified_email") && jsonNode.get("verified_email").asBoolean();

            if (email == null) {
                log.error("Không lấy được email từ Google API");
                return null;
            }


            return GoogleUserInfo.builder()
                    .email(email)
                    .name(name)
                    .picture(picture)
                    .givenName(givenName)
                    .familyName(familyName)
                    .emailVerified(emailVerified)
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi verify Google token: {}", e.getMessage(), e);
            return null;
        }
    }
}

