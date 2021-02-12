package crypto.webservices.api;

import com.coreoz.plume.jersey.security.permission.PublicApi;
import crypto.services.auth.AuthInfos;
import crypto.services.auth.AuthService;
import crypto.services.auth.Credentials;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Api("Manage authentication web-services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PublicApi
@Singleton
public class AuthWs {

    private final AuthService authService;

    @Inject
    public AuthWs(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @ApiOperation("Login")
    public AuthInfos login(Credentials credentials) {
        return authService.login(credentials.getUsername(), credentials.getPassword());
    }

}
