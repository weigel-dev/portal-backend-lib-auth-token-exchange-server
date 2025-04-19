package dev.weigel.authlib.controller;

import org.springframework.http.ResponseEntity;

public interface JwksHandler {
    ResponseEntity<String> getJwks();
}
