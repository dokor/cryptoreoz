package crypto.services.socket;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;

@AllArgsConstructor
public class SocketSession {

    private final Consumer<String> messageSender;
    private final Runnable close;
    private final String socketId;

    public String getSocketId() {
        return socketId;
    }

    public void close() {
        close.run();
    }

    public void sendMessage(SocketApplication socketApplication, String message) {
        sendMessage(socketApplication.name(), message);
    }

    public void sendMessage(String messagePrefixName, String message) {
        messageSender.accept(messagePrefixName + ":" + Strings.nullToEmpty(message));
    }

    public void sendMessage(String socketType, String messageType, String message) {
        messageSender.accept(socketType + ":" + messageType + ":" + Strings.nullToEmpty(message));
    }

}
