package com.moninfotech.controllers.room;

import com.moninfotech.domain.Room;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sayemkcn on 5/18/17.
 */
@RestController
@RequestMapping("/rest/rooms")
public class RoomRestCtrl {
    private final RoomService roomService;

    @Autowired
    public RoomRestCtrl(RoomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private ResponseEntity<Room> getRoom(@PathVariable("id") Long id) {
        Room room = this.roomService.findOne(id);
        if (room == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }
}
