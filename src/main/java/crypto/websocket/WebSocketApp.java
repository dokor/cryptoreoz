package crypto.websocket;

import com.coreoz.plume.services.time.TimeProvider;
import crypto.services.socket.SocketService;
import crypto.services.socket.SocketSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.SimpleWebSocket;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class WebSocketApp extends WebSocketApplication {

    private static final long PONG_RECEIVED = -1L;
    private static final byte[] SOCKET_PING = new byte[]{};

    private static final Logger logger = LoggerFactory.getLogger(WebSocketApp.class);

    private final Map<WebSocket, SocketInfo> sockets;
    private final SocketService socketService;
    private final TimeProvider timeProvider;

    @Inject
    public WebSocketApp(SocketService socketService, TimeProvider timeProvider) {
        this.socketService = socketService;
        this.timeProvider = timeProvider;
        this.sockets = new ConcurrentHashMap<>();
    }

    @Override
    public WebSocket createSocket(ProtocolHandler handler, HttpRequestPacket requestPacket,
                                  WebSocketListener... listeners) {
        return new SimpleWebSocket(
            handler,
            new WebSocketListenerApp(socketService, timeProvider, sockets)
        );
    }

    public void pingAndChecksSockets() {
        pingSockets();
        checksSockets();
    }

    private void pingSockets() {
        for (Map.Entry<WebSocket, SocketInfo> socket : sockets.entrySet()) {
            if (socket.getKey().isConnected()) {
                socket.getKey().sendPing(SOCKET_PING);
                if (socket.getValue().getLastPingSent() == PONG_RECEIVED) {
                    socket.getValue().setLastPingSent(timeProvider.currentTime());
                }
            } else {
                logger.debug("Déconnexion d'un websocket déjà deconnecté");
                socket.getKey().close();
            }
        }
    }

    private void checksSockets() {
        long socketTimeLimit = timeProvider.currentTime() - 5000;
        for (Map.Entry<WebSocket, SocketInfo> socket : sockets.entrySet()) {
            if (socket.getValue().getLastPingSent() != PONG_RECEIVED
                && socket.getValue().getLastPingSent() < socketTimeLimit) {
                logger.debug("Déconnexion d'un websocket ayant un trop grand ping");
                socket.getKey().close();
            }
        }
    }

    private static class WebSocketListenerApp implements WebSocketListener {
        private final SocketService socketService;
        private final TimeProvider timeProvider;
        private final Map<WebSocket, SocketInfo> sockets;

        private WebSocketListenerApp(SocketService socketService, TimeProvider timeProvider,
                                     Map<WebSocket, SocketInfo> sockets) {
            this.socketService = socketService;
            this.timeProvider = timeProvider;
            this.sockets = sockets;
        }

        @Override
        public void onClose(WebSocket socket, DataFrame frame) {
            SocketInfo socketInfo = sockets.remove(socket);
            socketService.disconnect(socketInfo.getId());
            logger.trace("A socket has been closed");
        }

        @Override
        public void onConnect(WebSocket socket) {
            String socketId = UUID.randomUUID().toString();
            sockets.put(socket, new SocketInfo(socketId, PONG_RECEIVED));
            socketService.connect(
                new SocketSession(message -> {
                    if (!socket.isConnected()) {
                        return;
                    }

                    try {
                        socket.send(message);
                    } catch (Exception e) {
                        logger.warn("Impossible d'envoyer {} au socket {}", message, socket.toString());
                    }
                },
                    socket::close,
                    socketId)
            );
            logger.trace("A socket has been opened");
        }

        @Override
        public void onMessage(WebSocket socket, String text) {
            try {
                SocketInfo socketInfo = sockets.get(socket);
                if (socketInfo == null) {
                    logger.error("Un message a été reçu via un socket qui n'a pas été déclaré");
                } else {
                    socketService.onMessage(socketInfo.getId(), text);
                }
            } catch (Exception e) {
                logger.warn("Cannot handle message", e);
            }
        }

        // unused

        @Override
        public void onMessage(WebSocket socket, byte[] bytes) {
        }

        @Override
        public void onPing(WebSocket socket, byte[] bytes) {
        }

        @Override
        public void onPong(WebSocket socket, byte[] bytes) {
            SocketInfo socketInfo = sockets.get(socket);
            if (socketInfo == null) {
                logger.error("Un message pong a été reçu via un socket qui n'a pas été déclaré");
            } else if (socketInfo.getLastPingSent() == PONG_RECEIVED) {
                logger.warn("Un message pong a été reçu alors qu'il n'y a pas de ping envoyé");
            } else {
                Instant currentTime = timeProvider.currentInstant();
                long latencyInMilliseconds = currentTime.toEpochMilli() - socketInfo.getLastPingSent();
                socketInfo.setLastPingSent(PONG_RECEIVED);
                socketService.onLatencyUpdate(socketInfo.getId(), latencyInMilliseconds >= 0 ? latencyInMilliseconds : 0);
            }
        }

        @Override
        public void onFragment(WebSocket socket, String fragment, boolean last) {
        }

        @Override
        public void onFragment(WebSocket socket, byte[] fragment, boolean last) {
        }

    }

    @AllArgsConstructor
    @Data
    private static class SocketInfo {
        private final String id;
        private long lastPingSent;
    }

}
