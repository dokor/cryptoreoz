package crypto.guice;

import com.coreoz.plume.admin.guice.GuiceAdminWsWithDefaultsModule;
import com.coreoz.plume.conf.guice.GuiceConfModule;
import com.coreoz.plume.db.querydsl.guice.GuiceQuerydslModule;
import com.coreoz.plume.jersey.guice.GuiceJacksonModule;
import com.google.inject.AbstractModule;
import crypto.jersey.JerseyConfigProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Group the Guice modules to install in the application
 */
public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new GuiceConfModule());
        install(new GuiceJacksonModule());
        // database & Querydsl installation
        install(new GuiceQuerydslModule());
        install(new GuiceAdminWsWithDefaultsModule());

        // prepare Jersey configuration
        bind(ResourceConfig.class).toProvider(JerseyConfigProvider.class);
    }

}
