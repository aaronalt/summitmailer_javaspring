package summitmail.utils;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public interface UserAuthFacade {
    Authentication getAuthentication();
}

