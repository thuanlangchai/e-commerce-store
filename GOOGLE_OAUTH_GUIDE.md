# Hướng Dẫn Chi Tiết: Google OAuth Login - Backend

## 📋 Tổng Quan

Hệ thống đã được tích hợp Google OAuth login. Khi user đăng nhập bằng Google:
1. Frontend lấy Google access token từ Google
2. Frontend gửi token này đến backend endpoint `/login/google`
3. Backend verify token và lấy thông tin user từ Google
4. Backend tạo hoặc tìm user trong database
5. Backend trả về JWT tokens (giống như login thông thường)

---

## 🔍 Cách Hoạt Động Chi Tiết

### 1. **Frontend → Backend: Gửi Google Token**

Frontend sử dụng `@react-oauth/google` để lấy Google access token, sau đó gửi đến backend:

```javascript
// Frontend gửi request
POST /api/auth/login/google
Body: { "token": "google-access-token-here" }
```

### 2. **Backend: Verify Token và Lấy User Info**

Backend nhận token và gọi Google UserInfo API:

**File: `GoogleTokenVerifier.java`**

```java
// Gọi Google API để lấy thông tin user
GET https://www.googleapis.com/oauth2/v2/userinfo?access_token={token}
```

Google API trả về JSON:
```json
{
  "email": "user@gmail.com",
  "name": "Nguyen Van A",
  "picture": "https://...",
  "verified_email": true,
  "given_name": "Van A",
  "family_name": "Nguyen"
}
```

### 3. **Backend: Tìm hoặc Tạo User**

**File: `LoginService.java` - Method `loginWithGoogle()`**

```java
// Bước 1: Verify token và lấy thông tin từ Google
GoogleUserInfo googleUserInfo = googleTokenVerifier.verifyToken(token);

// Bước 2: Tìm user trong database theo email
User user = userRepository.findByEmail(googleUserInfo.getEmail());

// Bước 3: Nếu chưa có user -> Tạo user mới
if (user == null) {
    user = createUserFromGoogleInfo(googleUserInfo);
}
```

**Tạo User Mới:**
- Email: từ Google
- Username: từ Google name (hoặc email nếu không có name)
- Password: random UUID (vì Google user không có password)
- Role: USER (mặc định)
- Phone: null (có thể cập nhật sau)
- Cart: tự động tạo cart cho user mới

### 4. **Backend: Tạo JWT Tokens**

Sau khi có user, backend tạo JWT tokens giống như login thông thường:

```java
// Tạo access token và refresh token
String accessToken = jwtUtil.generateAccessToken(...);
String refreshToken = jwtUtil.generateRefreshToken(...);

// Lưu token vào database
tokenService.createToken(accessToken);

// Trả về cho frontend
return LoginResponse(accessToken, refreshToken);
```

### 5. **Backend → Frontend: Trả Về JWT**

Response giống như `/login` thông thường:
```json
{
  "code": 200,
  "message": "Login with Google successfully!",
  "data": {
    "accessToken": "jwt-access-token",
    "refreshToken": "jwt-refresh-token"
  }
}
```

---

## 📁 Các File Đã Tạo/Cập Nhật

### Backend Files:

1. **`GoogleTokenVerifier.java`** (MỚI)
   - Verify Google access token
   - Gọi Google UserInfo API
   - Trả về `GoogleUserInfo`

2. **`GoogleUserInfo.java`** (MỚI)
   - DTO chứa thông tin user từ Google
   - Fields: email, name, picture, givenName, familyName, emailVerified

3. **`GoogleLoginRequest.java`** (MỚI)
   - Request DTO cho endpoint `/login/google`
   - Field: `token` (Google access token)

4. **`LoginService.java`** (CẬP NHẬT)
   - Thêm method `loginWithGoogle()`
   - Thêm method `createUserFromGoogleInfo()`

5. **`LoginController.java`** (CẬP NHẬT)
   - Thêm endpoint `POST /login/google`

6. **`SecurityConfig.java`** (CẬP NHẬT)
   - Permit endpoint `/login/google`

7. **`ErrorCode.java`** (CẬP NHẬT)
   - Thêm `EMAIL_NOT_VERIFIED`

8. **`pom.xml`** (CẬP NHẬT)
   - Thêm dependency `google-api-client`

---

## 🔐 Security & Best Practices

### 1. **Token Verification**
- Backend luôn verify token với Google API
- Không tin tưởng token từ frontend
- Chỉ chấp nhận token hợp lệ từ Google

### 2. **User Creation**
- Tự động tạo user nếu chưa tồn tại (auto-register)
- Email phải được verified bởi Google
- Password random (không bao giờ dùng đến)

### 3. **Token Management**
- Revoke tất cả token cũ trước khi tạo token mới
- Lưu access token vào database để quản lý

### 4. **Error Handling**
- Token không hợp lệ → `INCORRECT_EMAIL_PASSWORD`
- Email chưa verified → `EMAIL_NOT_VERIFIED`
- Lỗi Google API → Log và trả về null

---

## ⚙️ Cấu Hình

### `application.yaml`

Đã có sẵn Google Client ID:
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your-client-id-here
            client-secret: your-client-secret-here
```

**Lưu ý:** Client ID này phải giống với Client ID ở frontend (`.env` file)

---

## 🧪 Testing

### Test Flow:

1. **Frontend:** Click "Đăng nhập với Google"
2. **Google:** Hiển thị popup đăng nhập
3. **User:** Chọn tài khoản Google
4. **Frontend:** Nhận access token từ Google
5. **Frontend:** Gửi token đến `/api/auth/login/google`
6. **Backend:** Verify token, tạo/find user, trả về JWT
7. **Frontend:** Lưu JWT và đăng nhập thành công

### Test với Postman:

```bash
POST http://localhost:8080/api/auth/login/google
Content-Type: application/json

{
  "token": "ya29.a0AfH6SMBx..."
}
```

---

## ❓ FAQ

### Q: Tại sao không dùng Google ID Token?
A: `@react-oauth/google` chỉ trả về access token, không có ID token. Nên chúng ta dùng access token để gọi Google UserInfo API.

### Q: Password của Google user là gì?
A: Password là random UUID, không bao giờ được sử dụng. User chỉ đăng nhập bằng Google.

### Q: Nếu user đã có account với email đó thì sao?
A: Backend sẽ tìm user theo email và đăng nhập luôn, không tạo user mới.

### Q: Có thể đăng nhập bằng password sau khi đăng ký bằng Google không?
A: Không, vì password là random. User phải đăng nhập bằng Google hoặc reset password.

---

## 🐛 Troubleshooting

### Lỗi: "Token không hợp lệ"
- Kiểm tra Google Client ID ở frontend và backend có giống nhau không
- Kiểm tra token có hết hạn không
- Kiểm tra Google API có hoạt động không

### Lỗi: "Email is not verified"
- User phải verify email trên Google trước
- Kiểm tra `verified_email` trong response từ Google

### Lỗi: "Google API trả về lỗi"
- Kiểm tra internet connection
- Kiểm tra Google API có bị rate limit không
- Xem log để biết chi tiết lỗi

---

## 📚 Tài Liệu Tham Khảo

- [Google OAuth 2.0](https://developers.google.com/identity/protocols/oauth2)
- [Google UserInfo API](https://developers.google.com/identity/protocols/oauth2/openid-connect#obtainuserinfo)
- [@react-oauth/google](https://www.npmjs.com/package/@react-oauth/google)

