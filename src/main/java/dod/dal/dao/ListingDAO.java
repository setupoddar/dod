package dod.dal.dao;

import com.google.inject.Inject;
import dod.dal.model.Listing;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

/**
 * Created by setu.poddar on 27/06/17.
 */
@Slf4j
public class ListingDAO extends GenericDAO<Listing> {

    @Inject
    protected ListingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
