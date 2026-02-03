package com.minhthuan.web_store.domain.login_google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Service để verify Google OAuth access token và lấy thông tin user từ Google
 *
 * Cách hoạt động:
 * 1. Frontend gửi Google access token (từ @react-oauth/google)
 * 2. Backend gọi Google UserInfo API với access token này
 * 3. Google trả về thông tin user (email, name, picture, etc.)
 * 4. Backend sử dụng thông tin này để tạo/find user trong database
 *
 * Lưu ý: @react-oauth/google trả về access_token, không phải ID token
 * Nên chúng ta dùng access token để gọi Google UserInfo API
 */
@Slf4j
@Component
public class GoogleTokenVerifier {

    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Verify Google access token và lấy thông tin user từ Google UserInfo API
     *
     * @param accessToken: Google access token từ frontend (@react-oauth/google)
     * @return GoogleUserInfo chứa thông tin user từ Google, null nếu token không hợp lệ
     */
    public GoogleUserInfo verifyToken(String accessToken) {
        try {
            // Gọi Google UserInfo API với access token
            URL url = new URL(GOOGLE_USERINFO_URL + "?access_token=" + accessToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                log.error("Google API trả về lỗi: {}", responseCode);
                // Đọc error message nếu có
                try (InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        String errorMessage = new String(errorStream.readAllBytes());
                        log.error("Error message: {}", errorMessage);
                    }
                }
                return null;
            }

            // Đọc response từ Google API
            JsonNode jsonNode;
            try (InputStream inputStream = connection.getInputStream()) {
                jsonNode = objectMapper.readTree(inputStream);
            }

            // Extract thông tin user từ JSON response
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

            // Tạo GoogleUserInfo object
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

