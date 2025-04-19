# Portal Backend Lib - Auth Token Exchange Server

## Overview

This library provides a **token exchange server logic** for spring backend services.
It allows an application to:

- Validate external authentication tokens (e.g., Logto, Google, custom providers)
- Create internal access tokens and refresh tokens
- Manage user sessions independently from the external identity provider

The goal is to separate **identity validation** from **internal session management** - making the backend secure, flexible, and provider-agnostic.

## Features

- Pluggable `ExternalAuthenticationService` for multiple external providers
- Internal JWT Access Token creation
- Internal Refresh Token management
- Simple, extendable token validation and refresh cycle
- No embedded database, no hard-wired server - full flexibility for real applications

## Core Concepts

| Concept                                  | Description                                                       |
| ---------------------------------------- | ----------------------------------------------------------------- |
| `ExternalAuthenticationService`          | Interface for validating external identity tokens                 |
| `ExternalAuthenticationProviderResolver` | Dynamically resolves the correct authentication provider          |
| `AuthenticationService`                  | Exposes internal login and token refresh methods                  |
| `TokenService`                           | Handles generation and validation of internal JWTs                |
| DTOs                                     | Standard request/response objects for login and refresh workflows |

## How to Integrate

1. Add the library as a dependency:

```xml
<dependency>
  <groupId>dev.weigel</groupId>
  <artifactId>portal-backend-lib-auth-token-exchange-server</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. Provide your own implementations of `ExternalAuthenticationService`\
   (for example, `LogtoAuthenticationService`, `GoogleAuthenticationService`, etc.)

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

3. Wire the `AuthenticationService` into your controllers:

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refresh(request));
    }
}
```

## Expected API Payloads

### Login (POST `/auth/token`)

**Request Body:**

```json
{
  "externalToken": "external-access-token",
  "externalProvider": "logto"
}
```

**Response:**

```json
{
  "accessToken": "internal-access-token",
  "refreshToken": "internal-refresh-token"
}
```

### Refresh Token (POST `/auth/refresh`)

**Request Body:**

```json
{
  "refreshToken": "internal-refresh-token"
}
```

**Response:**

```json
{
  "accessToken": "new-internal-access-token",
  "refreshToken": "new-internal-refresh-token"
}
```

## License

This project is licensed under the MIT License.

## Authors

Developed and maintained by **portal@weigel.dev**.
