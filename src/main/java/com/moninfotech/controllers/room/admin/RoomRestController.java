package com.moninfotech.controllers.room.admin;

import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sayemkcn on 4/9/17.
 */
@RestController
@RequestMapping(value = "/rest/hotel/rooms")
public class RoomRestController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @Autowired
    public RoomRestController(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private ResponseEntity<List<Room>> allRooms(@CurrentUser User user) {
        return new ResponseEntity<>(this.hotelService.findByUser(user).getRoomList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Room> getRoom(@PathVariable("id") Long roomId) {
        Room room = this.roomService.findOne(roomId);
        if (room == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

}
