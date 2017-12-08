package dod;

import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import dod.config.DodConfiguration;
import dod.guice.DodModule;
import dod.health.ServiceHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;


public class DodServiceApplication extends Application<DodConfiguration> {

    private GuiceBundle<DodConfiguration> guiceBundle;

    private HibernateBundle<DodConfiguration> hibernateBundle;

    public static void main(String[] args) throws Exception {
        new DodServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "DOD";
    }

    @Override
    public void initialize(Bootstrap<DodConfiguration> bootstrap) {
        guiceBundle = new GuiceBundle.Builder<DodConfiguration>()
                .setConfigClass(DodConfiguration.class)
                .addModule(new DodModule(bootstrap))
                .enableAutoConfig(DodServiceApplication.class.getPackage().getName())
                .build(Stage.DEVELOPMENT);

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new SwaggerBundle<DodConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DodConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
    }

    @Override
    public void run(DodConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().setUrlPattern("/*");
        environment.healthChecks().register("ping", new ServiceHealthCheck());
    }
}
