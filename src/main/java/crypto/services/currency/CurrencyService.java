package crypto.services.currency;

import com.coreoz.plume.db.crud.CrudService;
import crypto.db.dao.CurrencyDao;
import crypto.db.generated.Currency;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrencyService extends CrudService<Currency> {

    private final CurrencyDao currencyDao;

    @Inject
    public CurrencyService(CurrencyDao currencyDao) {
        super(currencyDao);
        this.currencyDao = currencyDao;
    }

    public Currency findByCode(String code) {
        return currencyDao.findByCode(code);
    }
}

