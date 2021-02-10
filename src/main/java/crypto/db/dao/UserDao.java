package crypto.db.dao;

import com.coreoz.plume.db.querydsl.crud.CrudDaoQuerydsl;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import crypto.db.generated.QUser;
import crypto.db.generated.User;

import javax.inject.Inject;

public class UserDao extends CrudDaoQuerydsl<User> {
    @Inject
    UserDao(TransactionManagerQuerydsl transactionManager) {
        super(transactionManager, QUser.user);
    }
}
