package com.bkhome.dao;

import com.bkhome.persistence.Room;
import com.bkhome.persistence.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class RoomDao extends EntityDao<Room> {

    public List<Room> getRoomByUser(String userId) {
        Session session = openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Room> query = builder.createQuery(Room.class);
        Root<Room> root = query.from(Room.class);
        query.select(root);
        User user = new User();
        user.setId(userId);
        query.where(builder.equal(root.get("user"), user));
        return session.createQuery(query).getResultList();
    }
}
