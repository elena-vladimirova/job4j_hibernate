package ru.job4j.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.job4j.models.User;

import java.util.List;

public class HibernateRun {

    public static User create(User user, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
        return user;
    }

    public static void update(User user, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        User user = new User(null);
        user.setId(id);
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }

    public static List<User> findAll(SessionFactory sf) {
        Session session = sf.openSession();
        List result = session.createQuery("from User").list();
        session.close();
        return result;
    }

    public static User findById(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        User result = session.get(User.class, id);
        session.close();
        return result;
    }

    public static void main(String[] args) {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        User user = create(new User("Eugene"), sf);
        System.out.println(user);
        user.setName("Eugene Popov");
        update(user, sf);
        System.out.println(user);
        User rsl = findById(user.getId(), sf);
        System.out.println(rsl);
        delete(rsl.getId(), sf);
        List<User> list = findAll(sf);
        for (User it : list) {
            System.out.println(it);
        }
    }
}