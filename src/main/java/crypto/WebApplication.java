package crypto;

import java.io.IOException;

import crypto.services.binance.BinanceService;
import crypto.websocket.WebSocketApp;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crypto.guice.ApplicationModule;
import crypto.jersey.GrizzlySetup;

import com.coreoz.plume.jersey.guice.JerseyGuiceFeature;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

/**
 * The application entry point, where it all begins.
 */
public class WebApplication {

	private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

	public static void main(String[] args) throws IOException {
		long startTimestamp = System.currentTimeMillis();

		// initialize all application objects with Guice
		Injector injector = Guice.createInjector(Stage.PRODUCTION, new ApplicationModule());

		ResourceConfig jerseyResourceConfig = injector.getInstance(ResourceConfig.class);
		WebSocketApp webSocketApp = injector.getInstance(WebSocketApp.class);

		BinanceService binanceService = injector.getInstance(BinanceService.class);
		binanceService.testSurLesWebSockets();

		// enable Jersey to create objects through Guice Injector instance
		jerseyResourceConfig.register(new JerseyGuiceFeature(injector));
		// starts the server
		GrizzlySetup.start(
			jerseyResourceConfig,
			System.getProperty("http.port"),
			System.getProperty("http.address"),
			webSocketApp
		);

		logger.info("Server started in {} ms", System.currentTimeMillis() - startTimestamp);
	}

}
