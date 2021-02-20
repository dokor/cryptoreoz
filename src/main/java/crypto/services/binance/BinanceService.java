package crypto.services.binance;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import crypto.db.generated.Currency;
import crypto.db.generated.UserPlatform;
import crypto.guice.ApplicationModule;
import crypto.services.currency.CurrencyService;
import crypto.services.platform.PlatformService;
import crypto.services.socket.SocketService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class BinanceService {
    private static final Long ID = 1L;
    private final PlatformService platformService;
    private final CurrencyService currencyService;
    private final SocketService socketService;


    @Inject
    public BinanceService(PlatformService platformService, CurrencyService currencyService, SocketService socketService) {
        this.platformService = platformService;
        this.currencyService = currencyService;
        this.socketService = socketService;
    }

    private BinanceApiRestClient getClientInstance(Long idUser) {
        UserPlatform userPlatform = platformService.findByIdUserAndIdPlatform(idUser, ID);
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(userPlatform.getApiKey(), userPlatform.getSecretKey());
        return factory.newRestClient();
    }

    public void testSurLesWebSockets() {
        BinanceApiWebSocketClient webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();
        webSocketClient.onAggTradeEvent("btcusdt", new BinanceApiCallback<>() {
            @Override
            public void onResponse(final AggTradeEvent response) {
                socketService.broadcastMessage("FINANCE", response.getPrice());
            }

            @Override
            public void onFailure(final Throwable cause) {
                System.err.println("Web socket failed");
                cause.printStackTrace(System.err);
            }
        });
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
        binanceService.testSurLesWebSockets();
    }

}
