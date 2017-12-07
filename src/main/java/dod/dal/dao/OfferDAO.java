package dod.dal.dao;

import com.google.inject.Inject;
import dod.dal.exception.DBException;
import dod.dal.model.Offer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 * Created by setu.poddar on 27/06/17.
 */
@Slf4j
public class OfferDAO extends GenericDAO<Offer> {

    @Inject
    protected OfferDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Offer findByName(String name) throws DBException {
        log.info("fetch app by name - " + name);
        try {
            Criteria criteria = criteria();
            criteria.add(Restrictions.eq("endDate", name));
            return uniqueResult(criteria);
        } catch (Exception e) {
            log.error("Error while fetching app by name - " + name, e);
            throw new DBException("Error while fetching app by name - " + name, e);
        }
    }
}
