package com.mihaisavin.stox.dao.db;

import com.mihaisavin.stox.dao.UserDAO;
import com.mihaisavin.stox.domain.User;
import com.mihaisavin.stox.service.AlarmService;
import com.mihaisavin.stox.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public class HibernateUserDAO implements UserDAO<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmService.class);

    public User update(User user) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        session.persist(user);//persisting the object

        t.commit();//transaction is committed
        session.close();

        LOGGER.info("Successfully saved user in DB.");

        return user;
    }

    @Override
    public User findByUsername(String username) {

        List<User> result;

        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        Query query = session.createQuery("FROM User U WHERE U.username=:username");
        query.setParameter("username", username);
        result = query.list();

        t.commit();//transaction is committed
        session.close();

        if (result.size() > 1) {
            throw new IllegalStateException("Multiple users for username/email: " + username);
        }

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public String getUsersEmail(long userId) {

        String email;

        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        Query query = session.createQuery("SELECT U.email FROM User U WHERE U.id=:id");
        query.setParameter("id", userId);
        email = (String) query.uniqueResult();

        t.commit();//transaction is committed
        session.close();

        return email;
    }

}