package crypto.services.socket;

public interface SocketApplicationListener {

	default void onClose(SocketSession socketSession) {

	}

	void onMessage(SocketSession socketSession, String message);

}
