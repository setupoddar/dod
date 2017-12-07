package dod.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dod.config.DodConfiguration;
import dod.dal.dao.ProductDAO;
import dod.dal.model.*;
import dod.service.RatingService;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import org.hibernate.SessionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

@SuppressWarnings("unused")
public class DodModule extends AbstractModule {

    private HibernateBundle hibernateBundle = new HibernateBundle<DodConfiguration>(

            Offer.class, Tag.class, Listing.class, Product.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(DodConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public DodModule(Bootstrap<DodConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    protected void configure() {
        bind(ProductDAO.class);
        bind(RatingService.class);
    }

    @Singleton
    @Provides
    public SessionFactory getSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

    @Provides
    @Singleton
    public Pool<Jedis> getJedisPool(ManagedJedisPool jedisPool) {
        return jedisPool.getJedisPool();
    }

}