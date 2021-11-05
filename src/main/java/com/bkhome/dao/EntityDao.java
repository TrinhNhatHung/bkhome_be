package com.bkhome.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Repository
public class EntityDao<T> {

    @Autowired
    private SessionFactory sessionFactory;

    public Session openSession() {
        return sessionFactory.openSession();
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<T> getAll(Class<T> clazz) {
        Session session = openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    public T getById(Class<T> clazz, Serializable id) {
        Session session = openSession();
        return session.get(clazz, id);
    }

    public void insertOrUpdate(T t) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(t);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void delete(List<T> ts) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (T t : ts){
                session.delete(t);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            throw e;
        }
    }
}
