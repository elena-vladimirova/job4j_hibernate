package ru.job4j.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.job4j.models.User;

import java.util.List;
import java.util.function.Function;

public class HibernateRun {

    SessionFactory sf = new Configuration().configure().buildSessionFactory();

    public User create(User user) throws Exception {
        this.tx(session -> session.save(user));
        return user;
    }

    public void update(User user) throws Exception {
        this.tx(session -> {session.update(user);
                            return true;}
        );
    }

    public void delete(Integer id) throws Exception {
        User user = new User(null);
        user.setId(id);
        this.tx(session -> {session.delete(user);
                return true;}
        );
    }

    public List<User> findAll() throws Exception {
        return this.tx(session -> session.createQuery("from User").list());
    }

    public User findById(Integer id) throws Exception {
        return this.tx(session -> session.get(User.class, id));
    }

    private <T> T tx(final Function<Session, T> command) throws Exception {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) throws Exception {

        HibernateRun r = new HibernateRun();
        User user = r.create(new User("Eugene"));
        System.out.println(user);
        user.setName("Eugene Popov");
        r.update(user);
        System.out.println(user);
        User rsl = r.findById(user.getId());
        System.out.println(rsl);
        r.delete(rsl.getId());
        List<User> list = r.findAll();
        for (User it : list) {
            System.out.println(it);
        }
    }
}