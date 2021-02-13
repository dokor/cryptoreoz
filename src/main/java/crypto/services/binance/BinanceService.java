package crypto.services.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.market.TickerPrice;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import crypto.db.generated.Currency;
import crypto.db.generated.UserPlatform;
import crypto.guice.ApplicationModule;
import crypto.services.currency.CurrencyService;
import crypto.services.platform.PlatformService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class BinanceService {
    private static final Long ID = 1L;
    private final PlatformService platformService;
    private final CurrencyService currencyService;


    @Inject
    public BinanceService(PlatformService platformService, CurrencyService currencyService) {
        this.platformService = platformService;
        this.currencyService = currencyService;
    }

    private BinanceApiRestClient getClientInstance(Long idUser) {
        UserPlatform userPlatform = platformService.findByIdUserAndIdPlatform(idUser, ID);
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(userPlatform.getApiKey(), userPlatform.getSecretKey());
        return factory.newRestClient();
    }

    public List<AssetBalance> getWallet(Long idUser) {
        BinanceApiRestClient client = getClientInstance(idUser);
        Account account = client.getAccount();
        return account.getBalances()
            .stream()
            .filter(assetBalance -> Float.parseFloat(assetBalance.getFree()) > 0)
            .collect(Collectors.toList());
    }

    private void populateCurrency() {
        BinanceApiRestClient client = getClientInstance(1L);
        client.getAccount().getBalances().forEach(assetBalance -> {
            Currency currency = currencyService.findByCode(assetBalance.getAsset());
            if (currency == null) {
                currency = new Currency();
            }
            currency.setCode(assetBalance.getAsset());
            currencyService.save(currency);
        });

    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new ApplicationModule());
        BinanceService binanceService = injector.getInstance(BinanceService.class);
        System.out.println(binanceService.getWallet(1L));
    }


}

