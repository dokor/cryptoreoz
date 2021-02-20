package crypto.services.socket.finance;

import crypto.services.socket.SocketApplication;
import crypto.services.socket.SocketApplicationListener;
import crypto.services.socket.SocketSession;
import crypto.websocket.SocketMessageParser;
import crypto.websocket.SocketMessageParser.MessageParsed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Gère globalement les websockets finance
 */
@Singleton
public class FinanceSocketService implements SocketApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(FinanceSocketService.class);

    @Inject
    public FinanceSocketService() {
    }

    // actions génériques
    @Override
    public void onClose(SocketSession socketSession) {

    }

    @Override
    public void onMessage(SocketSession socketSession, String message) {
        // parsing du message (de la forme ACTION:message) et appel de l'action correspondante
        MessageParsed<FinanceSessionMessage> messageParsed = SocketMessageParser.parseMessageType(
            message,
            FinanceSessionMessage::valueOf
        );
        if (messageParsed == null) {
            logger.warn("Action video non reconnue dans le message {}", message);
            socketSession.sendMessage(SocketApplication.FINANCE, FinanceSessionResponses.MESSAGE_PARSING_ERROR);
            socketSession.close();
            return;
        }
    }

    @Value(staticConstructor = "of")
    static class SessionJoiningParameters {
        private final String sessionToken;
        private final long browserVersion;
        private final String userToken;
        private final String userName;
    }

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    @NoArgsConstructor
    static class SessionRawLogMessage {
        private String category;
        private String message;
        private String argument;
    }

}
