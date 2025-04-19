package dev.weigel.authlib.service;

/**
 * Provides a way to map external provider user IDs or email addresses
 * to an internal user ID for use in internal tokens and sessions.
 */
public interface UserMappingProvider {

    /**
     * Resolves the internal user ID based on the external user information.
     *
     * @param externalProvider The external provider (e.g., \"logto\", \"google\")
     * @param externalUserId   The user ID from the external provider
     * @param email            The email address, if available
     * @return The internal user ID
     * @throws RuntimeException if no mapping could be found
     */
    String resolveInternalUserId(String externalProvider, String externalUserId, String email);

}
