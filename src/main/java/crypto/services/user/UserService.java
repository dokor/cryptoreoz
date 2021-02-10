package crypto.services.user;

import com.coreoz.plume.db.crud.CrudService;
import crypto.db.dao.UserDao;
import crypto.db.generated.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserService extends CrudService<User> {


    @Inject
    public UserService(UserDao userDao) {
        super(userDao);
    }


}

