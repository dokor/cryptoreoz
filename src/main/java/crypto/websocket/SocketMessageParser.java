package crypto.websocket;

import lombok.Value;

import java.util.function.Function;

public class SocketMessageParser {

	public static<T> MessageParsed<T> parseMessageType(String message, Function<String, T> toMessageType) {
		int actionIndex = message.indexOf(':');
		if(actionIndex < 0) {
			actionIndex = message.length();
		}

		try {
			T messageType = toMessageType.apply(message.substring(0, actionIndex));
			if(messageType != null) {
				return new MessageParsed<>(
					messageType,
					actionIndex+1 > message.length() ? null : message.substring(actionIndex+1)
				);
			}
		} catch (IllegalArgumentException e) {
			// value not found
		}
		return null;
	}

	@Value
	public static class MessageParsed<T> {
		private final T type;
		private final String subMessage;
	}

}
