package crypto.services.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum SocketApplication {

	FINANCE(SocketApplicationListeners::financeSocket),
	;

	private final Function<SocketApplicationListeners, SocketApplicationListener> socketApplicationListener;

}
