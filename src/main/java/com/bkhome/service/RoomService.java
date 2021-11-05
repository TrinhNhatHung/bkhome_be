package com.bkhome.service;

import com.bkhome.dao.RoomDao;
import com.bkhome.persistence.Room;
import com.bkhome.persistence.RoomUtility;
import com.bkhome.persistence.Utility;
import com.bkhome.utils.DateUtils;
import com.bkhome.utils.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomService extends EntityService<Room> {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private FirebaseUtil firebaseUtil;

    public Map<String, Object> getDetailRoom(Integer roomId) {
        Room room = roomDao.getById(Room.class, roomId);
        Map<String, Object> result = new HashMap<>();

        result.put("title", room.getTitle());
        result.put("detail", room.getDetail());
        result.put("address", room.getAddress());
        result.put("price", room.getPrice());
        result.put("area", room.getArea());
        result.put("updateAt", DateUtils.toString(room.getUpdateAt(), "dd/MM/yyyy HH:mm:ss"));

        List<Map<String, Object>> imageUrls = room.getImages().stream()
                .map(image -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("link", firebaseUtil.getFileUrl(image.getLink()));
                    return map;
                })
                .collect(Collectors.toList());
        result.put("images", imageUrls);
        List<String> utilities = room.getRoomUtilities().stream()
                .map(RoomUtility::getUtility)
                .map(Utility::getName)
                .collect(Collectors.toList());
        result.put("utilities", utilities);

        Map<String, Object> user = new HashMap<>();
        user.put("fullname", room.getUser().getFullname());
        user.put("phone", room.getUser().getPhone());
        result.put("owner", user);
        return result;
    }

    public List<Map<String, Object>> getRooms() {
        List<Map<String, Object>> result;
        List<Room> rooms = roomDao.getAll(Room.class);
        result = rooms.stream().map(this::convertRoom).collect(Collectors.toList());
        return result;
    }

    public List<Map<String, Object>> getRoomByUser(String userId) {
        List<Room> rooms = roomDao.getRoomByUser(userId);
        return rooms.stream().map(this::convertRoom).collect(Collectors.toList());
    }

    private Map<String, Object> convertRoom(Room room) {
        Map<String, Object> map = new HashMap<>();
        map.put("roomId", room.getId());
        map.put("address", room.getAddress());
        map.put("price", room.getPrice());
        map.put("area", room.getArea());
        map.put("title", room.getTitle());
        map.put("updateAt", DateUtils.toString(room.getUpdateAt(), "dd/MM/yyyy HH:mm:ss"));
        String image = null;
        if (room.getImages().size() > 0) {
            image = firebaseUtil.getFileUrl(room.getImages().get(0).getLink());
        }
        map.put("image", image);
        return map;
    }

    public void updateOrInsert(Room room) {
        roomDao.insertOrUpdate(room);
    }

    public void delete(Integer... roomIds) {
        List<Room> rooms = Arrays.stream(roomIds).map(roomId -> {
            Room room = new Room();
            room.setId(roomId);
            return room;
        }).collect(Collectors.toList());
        roomDao.delete(rooms);
    }
}
