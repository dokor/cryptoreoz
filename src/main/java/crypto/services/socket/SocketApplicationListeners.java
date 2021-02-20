package crypto.services.socket;


import crypto.services.socket.finance.FinanceSocketService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SocketApplicationListeners {

    private final FinanceSocketService financeSocket;

    @Inject
    public SocketApplicationListeners(FinanceSocketService financeSocket) {
        this.financeSocket = financeSocket;
    }

    FinanceSocketService financeSocket() {
        return financeSocket;
    }

}
