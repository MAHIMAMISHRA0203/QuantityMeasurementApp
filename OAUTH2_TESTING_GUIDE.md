# OAuth2 + JWT Testing Guide

## Prerequisites

### 1. Google OAuth2 Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Go to "Credentials" → "Create Credentials" → "OAuth 2.0 Client IDs"
5. Set application type to "Web application"
6. Add authorized redirect URIs:
   - `http://localhost:8080/login/oauth2/code/google` (for production)
   - `http://localhost:8080/auth/login-success` (for our app)
7. Copy Client ID and Client Secret

### 2. Application Configuration

Update `src/main/resources/application.properties`:

```properties
# Google OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email

# JWT Configuration
app.jwt.secret=YourSuperSecretKeyForJWTChangeInProduction32Chars
app.jwt.expiration-seconds=3600
```

## Testing Methods

### Method 1: Browser Testing (Easiest)

1. **Start the application**:

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Test OAuth2 Login**:
   - Open browser: `http://localhost:8080/oauth2/authorization/google`
   - Sign in with Google account
   - Should redirect to `/auth/login-success` with JWT token

3. **Test Protected Endpoints**:
   - Copy the JWT token from the login success response
   - Use browser dev tools or curl to test protected endpoints

### Method 2: Postman Testing (Recommended)

#### Step 1: OAuth2 Authorization Code Flow

1. Open Postman
2. Create new request: `GET http://localhost:8080/oauth2/authorization/google`
3. This will redirect to Google OAuth2 login page

#### Step 2: Get JWT Token

After OAuth2 login, the app returns JWT token at `/auth/login-success`

#### Step 3: Test Protected Endpoints

Create requests with Authorization header:

**Get User Info:**

```
GET http://localhost:8080/api/users/me
Authorization: Bearer YOUR_JWT_TOKEN
```

**Compare Measurements (Public):**

```
POST http://localhost:8080/api/measurements/compare
Content-Type: application/json

{
  "value1": 1,
  "unit1": "FEET",
  "value2": 12,
  "unit2": "INCH",
  "measurementType": "LENGTH"
}
```

### Method 3: Curl Testing

#### 1. Start Application

```bash
./mvnw spring-boot:run
```

#### 2. OAuth2 Login (Manual)

- Open browser: `http://localhost:8080/oauth2/authorization/google`
- Complete Google login
- Copy JWT token from response

#### 3. Test Endpoints

**Test Public Endpoint:**

```bash
curl -X POST http://localhost:8080/api/measurements/compare \
  -H "Content-Type: application/json" \
  -d '{
    "value1": 1,
    "unit1": "FEET",
    "value2": 12,
    "unit2": "INCH",
    "measurementType": "LENGTH"
  }'
```

**Test Protected Endpoint:**

```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Test Unauthorized Access:**

```bash
curl -X GET http://localhost:8080/api/users/me
# Should return 401 Unauthorized
```

### Method 4: Integration Tests (Already Working)

The existing `AllEndpointsIntegrationTest.java` covers:

- ✅ Public endpoint access
- ✅ OAuth2 user authentication
- ✅ JWT bearer token authentication
- ✅ Unauthorized access handling

Run tests:

```bash
./mvnw test -Dtest=AllEndpointsIntegrationTest
```

## Complete Testing Flow

### 1. Unit Tests

```bash
./mvnw test
```

### 2. Integration Tests

```bash
./mvnw test -Dtest="*IntegrationTest"
```

### 3. Manual OAuth2 Flow

1. Configure Google OAuth2 credentials
2. Start app: `./mvnw spring-boot:run`
3. Browser: `http://localhost:8080/oauth2/authorization/google`
4. Login with Google
5. Extract JWT token
6. Test protected endpoints with Bearer token

### 4. API Testing with Postman

Import the following collection:

```json
{
  "info": {
    "name": "Quantity Measurement App",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Public - Compare Measurements",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"value1\":1,\"unit1\":\"FEET\",\"value2\":12,\"unit2\":\"INCH\",\"measurementType\":\"LENGTH\"}"
        },
        "url": {
          "raw": "http://localhost:8080/api/measurements/compare",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "measurements", "compare"]
        }
      }
    },
    {
      "name": "OAuth2 Login",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/oauth2/authorization/google",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["oauth2", "authorization", "google"]
        }
      }
    },
    {
      "name": "Protected - Get User Info",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{jwt_token}}"
          }
        ],
        "url": {
          "raw": "http://localhost:8080/api/users/me",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "users", "me"]
        }
      }
    }
  ],
  "variable": [
    {
      "key": "jwt_token",
      "value": "",
      "type": "string"
    }
  ]
}
```

## Expected Responses

### Successful Comparison:

```json
{
  "success": true,
  "statusCode": 200,
  "message": "Comparison completed: 1.0 FEET vs 12.0 INCH",
  "data": {
    "q1": "1.0 FEET",
    "q2": "12.0 INCH",
    "equal": true
  }
}
```

### Successful User Info:

```json
{
  "success": true,
  "statusCode": 200,
  "message": "Current user details",
  "data": {
    "id": "12345",
    "email": "user@gmail.com",
    "name": "John Doe",
    "picture": "https://...",
    "role": "USER"
  }
}
```

### Unauthorized Access:

```json
{
  "success": false,
  "statusCode": 401,
  "message": "Unauthorized",
  "data": null
}
```

## Troubleshooting

### Common Issues:

1. **"redirect_uri_mismatch"**: Add correct redirect URIs in Google Console
2. **"invalid_client"**: Check client ID/secret in application.properties
3. **JWT Token Issues**: Ensure 32+ character secret key
4. **Port Already in Use**: Kill existing process or change port

### Debug Mode:

Add to application.properties:

```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.example.quantity_measurement_app=DEBUG
```

This will show detailed OAuth2 and JWT logs.</content>
<parameter name="filePath">c:\Users\mishr\Downloads\quantity_measurement_app\quantity_measurement_app\OAUTH2_TESTING_GUIDE.md
