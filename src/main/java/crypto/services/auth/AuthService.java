package crypto.services.auth;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthService {


    @Inject
    public AuthService() {
    }


    public AuthInfos login(String username, String password) {
        //TODO implémenter le login (verification du user en bdd + genetation token)
        return new AuthInfos(1L, "1234", "Test user", "");
    }
}

