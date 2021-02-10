package crypto.webservices.api;

import com.coreoz.plume.jersey.security.permission.PublicApi;
import crypto.db.generated.User;
import crypto.services.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Api("Manage users web-services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PublicApi
@Singleton
public class UserWs {

    private final UserService userService;

    @Inject
    public UserWs(UserService userService) {
        this.userService = userService;
    }

    @GET
    @ApiOperation("Get All users")
    public Response getAll() {
        List<User> users = userService.findAll();
        return Response.ok(users)
            .header("Content-Range", "user 0-" + users.size() + "/" + users.size())
            .build();
    }

    @GET
    @ApiOperation("Get one user by id")
    @Path("/{id}")
    public User getById(@PathParam("id") Long id) {
        return userService.findById(id);
    }

}
