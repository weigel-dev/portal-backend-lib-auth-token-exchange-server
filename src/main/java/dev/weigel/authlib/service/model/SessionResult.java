package dev.weigel.authlib.service.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionResult {

    private final String internalUserId;
    private final Map<String, String> metadata;

}
