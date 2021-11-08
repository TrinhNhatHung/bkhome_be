package com.bkhome.api;

import com.bkhome.persistence.*;
import com.bkhome.service.RoomService;
import com.bkhome.service.UtilityService;
import com.bkhome.utils.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class RoomApi {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private FirebaseUtil firebaseUtil;

    @GetMapping(value = "/rooms")
    public ResponseEntity<?> getRooms() {
        List<Map<String, Object>> rooms = roomService.getRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping(value = "/detailRoom/{roomId}")
    public ResponseEntity<?> getDetailRoom(@PathVariable(name = "roomId") Integer roomId) {
        Map<String, Object> room = roomService.getDetailRoom(roomId);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @GetMapping(value = "/roomByUser")
    public ResponseEntity<?> getRoomByUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Map<String, Object>> rooms = roomService.getRoomByUser(userDetails.getUsername());
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @DeleteMapping(value = "deleteRooms")
    public ResponseEntity<?> deleteRoom(@RequestParam(name = "rooms") Integer[] roomIds) {
        System.out.println(Arrays.toString(roomIds));
        try {
            roomService.delete(roomIds);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Delete fail");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/addRoom", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addRoom(@ModelAttribute Room room,
                                     @RequestParam(name = "imageFiles", required = false) MultipartFile[] imageFiles,
                                     @RequestParam(name = "utilities", required = false) List<Integer> utilityIds) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            List<String> fileNames = firebaseUtil.uploadFile(imageFiles);
            List<Image> images = fileNames.stream().map(link -> {
                Image image = new Image();
                image.setLink(link);
                image.setRoom(room);
                return image;
            }).collect(Collectors.toList());

            room.setImages(images);
            User user = new User();
            user.setId(userDetails.getUsername());
            room.setUser(user);
            if (utilityIds != null) {
                List<RoomUtility> roomUtilities = utilityIds.stream().map(id -> {
                    Utility utility = utilityService.getById(id);
                    RoomUtility.Id roomUtilityId = new RoomUtility.Id(room.getId(), utility.getId());
                    return new RoomUtility(roomUtilityId, room, utility);
                }).collect(Collectors.toList());
                room.setRoomUtilities(roomUtilities);
            }
            roomService.updateOrInsert(room);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "Fail");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
