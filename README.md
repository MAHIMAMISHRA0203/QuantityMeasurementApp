# Quantity Measurement App

A Spring Boot application that provides quantity measurement comparison functionality with Google OAuth2 authentication and JWT token support.

## Features

- **Public Endpoints**: Quantity comparison without authentication
- **Protected Endpoints**: User information and measurement history (requires authentication)
- **Google OAuth2 Login**: Secure authentication via Google
- **JWT Tokens**: Bearer token authentication for API access
- **Database**: H2/MySQL support for data persistence

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Running the Application

1. **Clone/Download the project**
2. **Configure Google OAuth2** (optional for testing):
   - Set up a Google OAuth2 client in Google Cloud Console
   - Add your client ID and secret to `application.properties`:
     ```
     spring.security.oauth2.client.registration.google.client-id=your-client-id
     spring.security.oauth2.client.registration.google.client-secret=your-client-secret
     ```

3. **Run the application**:

   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**:
   - Public comparison: `POST /api/measurements/compare`
   - OAuth2 login: Visit `/oauth2/authorization/google`
   - User info: `GET /api/users/me` (requires Bearer token)

### API Endpoints

#### Public Endpoints

- `POST /api/measurements/compare` - Compare two quantities
  ```json
  {
    "value1": 1,
    "unit1": "FEET",
    "value2": 12,
    "unit2": "INCH",
    "measurementType": "LENGTH"
  }
  ```

#### Protected Endpoints (Require JWT Bearer Token)

- `GET /api/users/me` - Get current user information
- `GET /api/measurements/history` - Get measurement operation history

### Authentication Flow

1. **OAuth2 Login**: User visits `/oauth2/authorization/google`
2. **Token Generation**: After successful login, JWT token is generated
3. **API Access**: Use the JWT token in Authorization header: `Bearer <token>`

### Testing

Run all tests:

```bash
./mvnw test
```

Run specific integration tests:

```bash
./mvnw test -Dtest=AllEndpointsIntegrationTest
```

### Configuration

Key configuration files:

- `src/main/resources/application.properties` - Main configuration
- `src/test/resources/application-test.properties` - Test configuration

### Database

The application uses H2 for testing and can be configured for MySQL in production.

## Architecture

- **Spring Boot 3.3.5** - Framework
- **Spring Security** - Authentication and authorization
- **JWT (Nimbus JOSE)** - Token handling
- **JPA/Hibernate** - Data persistence
- **H2/MySQL** - Database
- **Maven** - Build tool
