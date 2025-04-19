package dev.weigel.authlib.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExternalAuthenticationProviderResolver {

    private final Map<String, ExternalAuthenticationService> providers;

    public ExternalAuthenticationProviderResolver(Map<String, ExternalAuthenticationService> providers) {
        this.providers = providers;
    }

    public ExternalAuthenticationService resolve(String externalProvider) {
        ExternalAuthenticationService service = providers.get(externalProvider);
        if (service == null) {
            throw new RuntimeException(
                    "No external authentication service registered for provider: " + externalProvider);
        }
        return service;
    }
}
