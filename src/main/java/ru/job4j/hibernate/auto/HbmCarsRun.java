package ru.job4j.hibernate.auto;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.job4j.hibernate.auto.model.Account;
import ru.job4j.hibernate.auto.model.Ad;

import java.util.HashSet;
import java.util.Set;

public class HbmCarsRun {
    public static void main(String[] args) {
        SessionFactory sf = new Configuration().configure("hibernateHQL.cfg.xml").buildSessionFactory();

        Session session;
        Transaction transaction = null;

        Account a = Account.of("testAcc1");

        Ad ad1 = Ad.of("testAd1");
        Ad ad2 = Ad.of("testAd2");
        Ad ad3 = Ad.of("testAd3");
        ad1.setPhotos(Set.of("1_1", "1_2"));
        ad2.setPhotos(Set.of("2_1", "2_2"));
        ad3.setPhotos(Set.of("3_1", "3_2"));

        try {
            session = sf.getCurrentSession();
            transaction = session.beginTransaction();

            session.persist(a);

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

            session1.saveOrUpdate(a);
            ad1.setOwner(a);
            ad2.setOwner(a);
            ad3.setOwner(a);

            Set<Ad> ads = new HashSet<>();
            ads.add(ad1);
            ads.add(ad2);
            ads.add(ad3);

            a.setAdList(ads);

            session1.persist(ad1);
            session1.persist(ad2);
            session1.persist(ad3);

            session1.merge(a);

            transaction1.commit();
        }  catch (Exception e) {
            e.printStackTrace();
        }


        Session session2;
        Transaction transaction2 = null;
        try {
            session2 = sf.getCurrentSession();
            transaction2 = session2.beginTransaction();

            Account account = (Account)
            session2.createQuery("select distinct a from Account a left join fetch a.adList list left join fetch list.photos where a.id = 1").uniqueResult();

            System.out.println(account);

            transaction2.commit();
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }
}
