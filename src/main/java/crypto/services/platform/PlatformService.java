package crypto.services.platform;

import crypto.db.dao.UserPlatformDao;
import crypto.db.generated.UserPlatform;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlatformService {

    private final UserPlatformDao userPlatformDao;

    @Inject
    public PlatformService(UserPlatformDao userPlatformDao) {
        this.userPlatformDao = userPlatformDao;
    }

    public UserPlatform findByIdUserAndIdPlatform(Long idUser, Long idPlatform) {
        return userPlatformDao.findByIdUserAndIdPlatform(idUser, idPlatform);
    }


}

