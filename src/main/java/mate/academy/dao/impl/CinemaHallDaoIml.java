package mate.academy.dao.impl;

import mate.academy.dao.CinemaHallDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.CinemaHall;
import mate.academy.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class CinemaHallDaoIml implements CinemaHallDao {
    private static final String GET_ALL_QUERY = "SELECT * FROM cinema_hall";
    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(cinemaHall);
            transaction.commit();
            return cinemaHall;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Failed to add cinema hall to DB " + cinemaHall, e);
        }
    }

    @Override
    public Optional<CinemaHall> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(CinemaHall.class, id));
        } catch (HibernateException e) {
            throw new DataProcessingException("Failed to get CinemaHall by id " + id, e);
        }
    }

    @Override
    public Optional <List<CinemaHall>> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createNativeQuery(GET_ALL_QUERY, CinemaHall.class)
                    .getResultList());
        } catch (HibernateException e) {
            throw new DataProcessingException("Failed to fetch all the CinemaHalls from BD", e);
        }
    }
}
