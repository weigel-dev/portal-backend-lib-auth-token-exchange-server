package dev.weigel.authlib.service;

import java.util.Map;

public interface ClaimsProvider {

    /**
     * Loads custom claims that should be added to the JWT for a user.
     *
     * @param internalUserId the internal user ID
     * @return a map of claims (String key, String value) to be added into the token
     */
    Map<String, String> provideClaims(String internalUserId);
}
