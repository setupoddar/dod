package dod.dal.dao;

import com.google.inject.Inject;
import dod.dal.model.Listing;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by setu.poddar on 27/06/17.
 */
@Slf4j
public class ListingDAO extends GenericDAO<Listing> {

    @Inject
    protected ListingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Listing> getProducts(List<String> listings){
        return criteria().add(Restrictions.in("id", listings)).list();
    }

}
