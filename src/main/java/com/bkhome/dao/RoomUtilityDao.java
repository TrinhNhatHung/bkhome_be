package com.bkhome.dao;

import com.bkhome.persistence.Room;
import com.bkhome.persistence.RoomUtility;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

@Repository
public class RoomUtilityDao extends EntityDao<RoomUtilityDao> {

    public void delete(Room room) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<RoomUtility> query = builder.createCriteriaDelete(RoomUtility.class);
        Root<RoomUtility> root = query.from(RoomUtility.class);
        query.where(builder.equal(root.get("room"), room));
        try {
            session.createQuery(query).executeUpdate();
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
            throw e;
        }
    }
}
