package ru.job4j.hibernate.poly.s3;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HbmS3Run {
    public static void main(String[] args) {
        SessionFactory sf = new Configuration().configure("hibernatePolyS3.cfg.xml").buildSessionFactory();

        Session session;
        Transaction transaction = null;
        try {
            session = sf.getCurrentSession();
            transaction = session.beginTransaction();

            User u = new User();
            u.setAddress("address");
            u.setPhone("phone");
            u.setName("name");

            Book b = new Book();
            b.setAuthor("author");
            b.setPages(300L);
            b.setName("name");

            session.persist(u);
            session.persist(b);
            transaction.commit();
        }  catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        Session session1;
        Transaction transaction1 = null;
        try {
            session1 = sf.getCurrentSession();
            transaction1 = session1.beginTransaction();
            System.out.println("###########################");
            session1.createQuery("from " + BaseEntity.class.getSimpleName(), BaseEntity.class).list().forEach(System.out::println);
            System.out.println("###########################");
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
