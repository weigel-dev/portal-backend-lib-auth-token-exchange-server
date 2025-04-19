package dev.weigel.authlib.controller;

import dev.weigel.authlib.controller.model.AuthRequest;
import dev.weigel.authlib.controller.model.AuthResponse;
import dev.weigel.authlib.controller.model.RefreshRequest;
import dev.weigel.authlib.controller.model.RefreshResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationController {

    ResponseEntity<AuthResponse> login(AuthRequest request);

    ResponseEntity<RefreshResponse> refresh(RefreshRequest request);

}
