package crypto.db.dao;

import com.coreoz.plume.db.querydsl.crud.CrudDaoQuerydsl;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import crypto.db.generated.Currency;
import crypto.db.generated.QCurrency;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrencyDao extends CrudDaoQuerydsl<Currency> {

    @Inject
    public CurrencyDao(TransactionManagerQuerydsl transactionManager) {
        super(transactionManager, QCurrency.currency);
    }

    public Currency findByCode(String code) {
        return transactionManager.selectQuery()
            .select(QCurrency.currency)
            .from(QCurrency.currency)
            .where(QCurrency.currency.code.eq(code))
            .fetchFirst();
    }
}

