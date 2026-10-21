package com.example.userservice.console.dao;

import com.example.userservice.common.model.User;
import java.util.List;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User user) {
        log.info("Saving user: email={}", user.getEmail());
        return doTransactional(session -> {
            session.persist(user);
            return user;
        });
    }

    @Override
    public User getById(Long id) {
        log.debug("Getting user by id={}", id);
        return doTransactional(session -> session.find(User.class, id));
    }

    @Override
    public User getByEmail(String email) {
        log.debug("Getting user by email={}", email);
        return doTransactional(session ->
                session.createQuery("FROM User WHERE email = :email", User.class)
                        .setParameter("email", email)
                        .uniqueResult()
        );
    }

    @Override
    public List<User> getAll() {
        log.debug("Getting all users");
        return doTransactional(session -> session.createQuery("FROM User", User.class).list());
    }

    @Override
    public User update(User user) {
        log.info("Updating user: id={}", user.getId());
        return doTransactional(session -> session.merge(user));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting user: id={}", id);
        doTransactional(session -> {
            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            return null;
        });
    }

    private <R> R doTransactional(Function<Session, R> action) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            R result = action.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    log.warn("Rollback failed: {}", rollbackEx.getMessage());
                }
            }
            log.error("Transaction failed", e);
            throw e;
        }
    }
}