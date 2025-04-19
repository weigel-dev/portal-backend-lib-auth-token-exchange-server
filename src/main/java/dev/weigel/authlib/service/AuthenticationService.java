package dev.weigel.authlib.service;

import dev.weigel.authlib.controller.model.AuthRequest;
import dev.weigel.authlib.controller.model.AuthResponse;
import dev.weigel.authlib.controller.model.RefreshRequest;
import dev.weigel.authlib.controller.model.RefreshResponse;

public interface AuthenticationService {

    AuthResponse authenticate(AuthRequest body);

    RefreshResponse refresh(RefreshRequest body);

}
