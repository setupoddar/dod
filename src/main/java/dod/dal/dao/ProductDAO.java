package dod.dal.dao;

import com.google.inject.Inject;
import dod.dal.model.Listing;
import dod.dal.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by setu.poddar on 27/06/17.
 */
@Slf4j
public class ProductDAO extends GenericDAO<Listing> {


    @Inject
    protected ProductDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Product> getProducts(List<String> productIds){
        return criteria().add(Restrictions.in("id", productIds)).list();
    }

}
