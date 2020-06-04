package ru.job4j.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.job4j.models.Comment;
import ru.job4j.models.Item;
import ru.job4j.models.User;

import java.util.List;
import java.util.function.Function;

public class HibernateRun {

    SessionFactory sf = new Configuration().configure().buildSessionFactory();

    public <T> T create(T obj) throws Exception {
        this.tx(session -> session.save(obj));
        return obj;
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

    public User findUserById(Integer id) throws Exception {
        return this.tx(session -> session.get(User.class, id));
    }

    public Item findItemById(Integer id) throws Exception {
        return this.tx(session -> session.get(Item.class, id));
    }

    public List<Item> findItemByCridentional(String author) throws Exception {
        return this.tx(session -> {List<Item> result = null;
                                   Query queryAuthorId = session.createQuery("select u.id from User u where u.name = :author");
                                   queryAuthorId.setParameter("author", author);
                                   Integer authorId = (Integer)queryAuthorId.uniqueResult();
                                   if (authorId != null) {
                                       Query queryItem = session.createQuery("select i from Item i where i.author = :authorId");
                                       queryItem.setParameter("authorId", authorId);
                                       result = queryItem.list();
                                   }
                                   return result;
                                  }
                      );
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
        /*User user = r.create(new User("Eugene"));
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
        }*/

        Item item = new Item();
        item.setDesc("Err");
        item.setAuthor(r.create(new User("Eugene")));
        r.create(item);
        Item rsl = r.findItemById(item.getId());
        System.out.println(item);

        Comment comment = new Comment("Done");
        comment.setItem(item);
        r.create(comment);

        rsl = r.findItemById(item.getId());
        System.out.println(item);
        System.out.println(rsl.getComments());

        List<Item> items = r.findItemByCridentional("Eugene");
        items.stream().forEach(i -> System.out.println(i));

    }
}