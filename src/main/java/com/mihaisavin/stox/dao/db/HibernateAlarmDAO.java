package com.mihaisavin.stox.dao.db;

import com.mihaisavin.stox.dao.AlarmDAO;
import com.mihaisavin.stox.domain.Alarm;
import com.mihaisavin.stox.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Primary
@Repository
public class HibernateAlarmDAO implements AlarmDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateAlarmDAO.class);

    @Override
    public Collection<String> getWatchedSymbols() {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        Set<String> watchedSymbols = new HashSet<>();
        watchedSymbols.addAll(session.createQuery("SELECT A.symbol FROM Alarm A WHERE A.active = true").list());

        t.commit();//transaction is committed
        session.close();

        return watchedSymbols;
    }

    @Override
    public Collection<Alarm> getActiveAlarms() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        Collection<Alarm> activeAlarms = new LinkedList<>();
        activeAlarms.addAll(session.createQuery("FROM Alarm A WHERE A.active = true").list());

        t.commit();//transaction is committed
        session.close();

        return activeAlarms;
    }

    @Override
    public Collection<Alarm> getAll(long ownerId) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        Query query = session.createQuery("FROM Alarm A WHERE A.ownerId = :ownerId");
        query.setParameter("ownerId", ownerId);

        List<Alarm> alarmList = query.list();

        t.commit();//transaction is committed
        session.close();
        return alarmList;
    }

    @Override
    public Alarm update(Alarm alarm) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        session.saveOrUpdate(alarm);//persisting the object

        t.commit();//transaction is committed
        session.close();

        LOGGER.info("successfully saved active alarm");

        return alarm;
    }

    @Override
    public Alarm findById(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        Query query = session.createQuery("FROM Alarm A WHERE A.id = :id");
        query.setParameter("id", id);

        Alarm alarm = (Alarm) query.uniqueResult();

        t.commit();//transaction is committed
        session.close();

        return alarm;
    }

    @Override
    public boolean delete(Alarm alarm) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
//        Session session=factory.openSession();
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        session.delete(alarm);//persisting the object

        t.commit();//transaction is committed
        session.close();

        LOGGER.info("Successfully deleted " + alarm.getSymbol() + " alarm.");

        return true;
    }

    @Override
    public Collection<Alarm> searchByName(String query) {
        //TODO implement search function
        return null;
    }

}
