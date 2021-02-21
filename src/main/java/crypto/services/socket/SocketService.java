package crypto.services.socket;

import com.coreoz.plume.services.time.TimeProvider;
import crypto.websocket.SocketMessageParser;
import crypto.websocket.SocketMessageParser.MessageParsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Couche d'abstraction d'accès aux websockets pour ne pas être dépendent
 * de l'implémentation websocket (Tomcat, Grizzly etc.).
 * <p>
 * Permet également de faire le routing des messages des sockets vers les
 * différents modules consommateur. Cela permet notamment de ne pas avoir
 * à réimplementer la logique technique propre à chaque socket pour
 * chaque module qui utilise des websockets (chat, webrtc, alert, etc.).
 */
@Singleton
public class SocketService {

    private static final Logger logger = LoggerFactory.getLogger(SocketService.class);

    // le temps après la connexion d'une nouvelle websocket
    // pour laquelle la websocket sera automatiquement déconnectée si elle n'a pas envoyé de message
    private static final Duration IDLE_SOCKET_AFTER_CONNECTION_TIMEOUT = Duration.ofSeconds(10);

    private final SocketApplicationListeners socketApplicationListeners;
    private final TimeProvider timeProvider;

    private final Map<String, SocketSession> sockets = new ConcurrentHashMap<>();
    private final Map<String, Instant> socketThatHasNotSentMessageYet = new ConcurrentHashMap<>();

    @Inject
    public SocketService(SocketApplicationListeners socketApplicationListeners, TimeProvider timeProvider) {
        this.socketApplicationListeners = socketApplicationListeners;
        this.timeProvider = timeProvider;
    }

    /**
     * Appelé à chaque fois qu'un socket est déconnecté
     */
    public void disconnect(String socketId) {
        SocketSession socketSession = sockets.get(socketId);
        if (socketSession != null) {
            for (SocketApplication socketApplication : SocketApplication.values()) {
                socketApplication.getSocketApplicationListener().apply(socketApplicationListeners).onClose(socketSession);
            }
            sockets.remove(socketId);
        }

        socketThatHasNotSentMessageYet.remove(socketId);
    }

    /**
     * Appelé à chaque fois qu'un socket est connecté
     */
    public void connect(SocketSession socketSession) {
        sockets.put(socketSession.getSocketId(), socketSession);
        socketThatHasNotSentMessageYet.put(socketSession.getSocketId(), timeProvider.currentInstant());
    }

    /**
     * Appelé à chaque fois qu'un message est reçu par un socket
     */
    public void onMessage(String socketId, String message) {
        SocketSession socketSession = sockets.get(socketId);
        if (socketSession == null) {
            logger.warn("Socket non trouvé pour l'id {}", socketId);
            return;
        }

        // un message a bien été envoyé par le socket
        socketThatHasNotSentMessageYet.remove(socketId);

        // parsing du message (de la forme APPLICATION:message) et appel de l'action correspondante
        MessageParsed<SocketApplication> messageParsed = SocketMessageParser.parseMessageType(message, SocketApplication::valueOf);
        if (messageParsed == null) {
            logger.warn("Application socket non reconnue dans le message {}", message);
            socketSession.close();
            return;
        }

        messageParsed.getType().getSocketApplicationListener().apply(socketApplicationListeners).onMessage(
            socketSession,
            messageParsed.getSubMessage()
        );
    }

    public void onLatencyUpdate(String socketId, long milliseconds) {
        SocketSession socketSession = sockets.get(socketId);
        if (socketSession == null) {
            logger.warn("Socket non trouvé pour l'id {}", socketId);
            return;
        }

        socketSession.sendMessage("LATENCY", String.valueOf(milliseconds));
    }


    public void broadcastMessage(String socketType, String messageType, String message) {
        sockets.forEach((id, socketSession) -> {
            socketSession.sendMessage(socketType, messageType, message);
        });

    }

}
