package crypto.db.dao;

import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import crypto.db.generated.QUserPlatform;
import crypto.db.generated.UserPlatform;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserPlatformDao {

    private final TransactionManagerQuerydsl transactionManager;

    @Inject
    public UserPlatformDao(TransactionManagerQuerydsl transactionManager) {
        this.transactionManager = transactionManager;
    }


    public UserPlatform findByIdUserAndIdPlatform(Long idUser, Long idPlatform) {
        return transactionManager.selectQuery()
            .select(QUserPlatform.userPlatform)
            .from(QUserPlatform.userPlatform)
            .where(QUserPlatform.userPlatform.idUser.eq(idUser))
            .where(QUserPlatform.userPlatform.idPlatform.eq(idPlatform))
            .fetchFirst();
    }


}

