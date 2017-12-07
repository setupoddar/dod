package dod.dal.dao;

import com.google.inject.Inject;
import dod.dal.exception.DBException;
import dod.dal.model.GenericModel;
import dod.dal.model.TimestampEntity;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;

import java.util.Date;

@Slf4j
abstract class GenericDAO<T extends GenericModel> extends AbstractDAO<T> {

    @Inject
    protected GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdate(T object) throws DBException {
        try {
            if (object != null) {
                if (object.getClass().getSuperclass() == TimestampEntity.class) {
                    TimestampEntity t = (TimestampEntity) object;
                    t.setLastModified(new Date());
                    if (t.getCreatedOn() == null)
                        t.setCreatedOn(new Date());
                }
                persist(object);
            }
        } catch (Exception e) {
            log.error("Unable to save or update object", e);
            throw new DBException("Unable to save or update object", e);
        }
    }


    public void save(T object) throws DBException {
        try {
            if (object != null) {
                if (StringUtils.isNotEmpty(object.getId()))
                    throw new DBException("Id already present");
                if (object.getClass().getSuperclass() == TimestampEntity.class) {
                    TimestampEntity t = (TimestampEntity) object;
                    t.setLastModified(new Date());
                    t.setCreatedOn(new Date());
                }
                persist(object);
            }
        } catch (Exception e) {
            log.error("Unable to save object", e);
            throw new DBException("Unable to save object", e);
        }
    }


    public void update(T object) throws DBException {
        try {
            if (object != null) {
                if (StringUtils.isEmpty(object.getId()))
                    throw new DBException("Id missing");
                if (object.getClass().getSuperclass() == TimestampEntity.class) {
                    TimestampEntity t = (TimestampEntity) object;
                    t.setLastModified(new Date());
                }
                persist(object);
            }
        } catch (Exception e) {
            log.error("Unable to update object", e);
            throw new DBException("Unable to update object", e);
        }
    }


    public T findById(String id) throws DBException {
        try {
            return get(id);
        } catch (Exception e) {
            throw new DBException("Unable to find object with - " + id, e);
        }
    }

    public void delete(T object) throws DBException {
        try {
            if (object != null) {
                currentSession().delete(object);
            }
        } catch (Exception e) {
            log.error("Unable to delete object", e);
            throw new DBException("Unable to delete object ", e);
        }
    }
}
