package crypto.webservices.api;

import com.coreoz.plume.admin.db.generated.AdminUser;
import com.coreoz.plume.admin.jersey.feature.RestrictToAdmin;
import com.coreoz.plume.admin.services.user.AdminUserService;
import com.coreoz.plume.admin.websession.WebSessionAdmin;
import crypto.webservices.AdminPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Api("Manage users web-services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RestrictToAdmin(AdminPermissions.MANAGE_USERS)
@Singleton
public class UserWs {

    private final AdminUserService adminUserService;

    @Inject
    public UserWs(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GET
    @ApiOperation("Get All users")
    public Response getAll(@Context WebSessionAdmin webSessionAdmin) {
        // TODO ne pas tout renvoyer, faire un bean avec juste les informations utiles
        List<AdminUser> users = adminUserService.findAll();
        return Response.ok(users)
            .header("Content-Range", "user 0-" + users.size() + "/" + users.size())
            .build();
    }

    @GET
    @ApiOperation("Get one user by id")
    @Path("/{id}")
    public AdminUser getById(@PathParam("id") Long id) {
        // TODO ne pas tout renvoyer, faire un bean avec juste les informations utiles
        return adminUserService.findById(id);
    }

}
