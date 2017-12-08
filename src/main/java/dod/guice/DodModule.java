package dod.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dod.config.DodConfiguration;
import dod.dal.dao.ProductDAO;
import dod.dal.model.Listing;
import dod.dal.model.Offer;
import dod.dal.model.Product;
import dod.dal.model.Tag;
import dod.service.FederatorService;
import dod.service.RatingService;
import dod.service.ZuluService;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import org.hibernate.SessionFactory;

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
        bind(FederatorService.class);
        bind(ZuluService.class);
        bind(ObjectMapper.class);
    }

    @Singleton
    @Provides
    public SessionFactory getSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

}
