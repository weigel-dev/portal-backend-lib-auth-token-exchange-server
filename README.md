# Portal Backend Lib - Auth Token Exchange Server

> Disclaimer: No testing yet. A lot of the core code is generated for POC preparation.

## Overview

This library provides **token exchange server logic** for Spring Boot backend services.  
It allows an application to:

- Validate external authentication tokens (e.g., Logto, Google, custom providers)
- Create internal access tokens (JWT) and refresh tokens
- Manage user sessions independently from external identity providers

The goal is to **separate identity validation from internal session management**,  
making the backend **secure, flexible, and provider-agnostic**.

## Features

- Pluggable `ExternalAuthenticationService` for multiple external providers
- Internal JWT Access Token generation
- Internal Refresh Token management (with rotation)
- Session management via a service interface
- Public JWKS endpoint for access token validation
- Lightweight, no embedded server or database required
- No Spring Web dependencies in core library (transport-agnostic)

## Core Concepts

| Concept                                    | Description                                        |
| :----------------------------------------- | :------------------------------------------------- |
| `ExternalAuthenticationService`            | Interface to validate external identity tokens     |
| `ExternalAuthenticationProviderResolver`   | Resolve the correct provider dynamically           |
| `AuthenticationService`                    | Core service exposing login and refresh logic      |
| `TokenService`                             | Create and validate internal JWTs                  |
| `SessionService`                           | Interface for managing sessions and refresh tokens |
| `ClaimsProvider`                           | Interface to dynamically load claims for the JWT   |
| DTOs (`AuthRequest`, `AuthResponse`, etc.) | Clean request and response objects for integration |

## How to Integrate

### 1. Add the Library as a Dependency

```xml
<dependency>
  <groupId>dev.weigel.portal</groupId>
  <artifactId>backend-lib-auth-token-exchange-server</artifactId>
  <version>{{ VERSION }}</version>
</dependency>
```

(Adjust the version as needed.)

### 2. Implement Required Interfaces

You must implement the following interfaces in your service:

| Interface                       | Purpose                                                           |
| :------------------------------ | :---------------------------------------------------------------- |
| `ExternalAuthenticationService` | Validate external tokens (Logto, Google, etc.)                    |
| `SessionService`                | Store, validate, invalidate sessions and refresh tokens           |
| `UserMappingProvider`           | Map external user information to internal user IDs                |
| `ClaimsProvider`                | Provide claims to be included in issued JWTs                      |
| `PublicKeyProvider` (optional)  | Provide public keys for JWKS endpoint (optional for dynamic keys) |

Example for `ExternalAuthenticationService`:

```java
@Component("logto")
public class LogtoAuthenticationService implements ExternalAuthenticationService {
    @Override
    public ExternalAuthenticationResult validate(String externalToken) {
        // Validate via Logto
        return new ExternalAuthenticationResult(true, "logto-user-id", "user@example.com");
    }
}
```

### 3. Implement Authentication and Transport Handling

Implement the controller layer to:

- Use the `AuthenticationService`
- Set the refresh token into an **HttpOnly Secure Cookie**
- Read refresh token from the Cookie during refresh
- Clear the refresh token Cookie during logout

Example (simplified):

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authenticationService.authenticate(request);
        // Set HttpOnly cookie for refreshToken
        return ResponseEntity.ok(new AuthResponse(authResponse.getAccessToken(), null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        // Read refresh token from cookie
        RefreshResponse refreshResponse = authenticationService.refreshFromToken(refreshToken);
        // Set new refresh token cookie
        return ResponseEntity.ok(new RefreshResponse(refreshResponse.getAccessToken(), null));
    }
}
```

## Expected API Payloads

### Login (POST `/auth/token`)

**Request Body:**

```json
{
  "externalToken": "external-access-token",
  "externalProvider": "logto",
  "environmentMetadata": {
    "env": "production"
  }
}
```

**Response:**

```json
{
  "accessToken": "internal-access-token"
}
```

### Refresh (POST `/auth/refresh`)

**No body needed if refresh token is stored in cookie.**

**Response:**

```json
{
  "accessToken": "new-internal-access-token"
}
```

## Service Responsibilities

| Area                                              | Handled by             |
| :------------------------------------------------ | :--------------------- |
| Set refresh token as HttpOnly cookie              | Service controller     |
| Read refresh token from Cookie on refresh         | Service controller     |
| Clear refresh token Cookie on logout              | Service controller     |
| Implement Logout endpoint (optional)              | Service                |
| Handle CORS configuration for credentials         | Service configuration  |
| Load public/private keys for JWT signing          | Service bootstrapping  |
| Secure key storage (local or AWS Secrets Manager) | Service runtime config |

## Security Best Practices

- Use `Secure`, `HttpOnly`, `SameSite=Strict` cookies for refresh tokens
- Rotate refresh tokens on every refresh
- Store refresh tokens hashed
- Serve APIs over HTTPS only
- Enforce CORS with allowed credentials
- Consider key rotation for long-term signing keys

## License

This project is licensed under the MIT License.

## Maintainer

Developed and maintained by **portal@weigel.dev**.
