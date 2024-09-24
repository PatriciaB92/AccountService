package account;

import org.springframework.security.core.Authentication;

public interface AppAuthentication extends Authentication {
    Object getPrincipal();
    Object getCredentials();
    Object getDetails();
    boolean isAuthenticated();
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}
