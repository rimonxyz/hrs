package com.moninfotech.controllers.room.admin;

import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by sayemkcn on 4/9/17.
 */
@RestController
@RequestMapping(value = "/rest/hotel/rooms")
public class RoomRestController {

    @Autowired
    private HotelService hotelService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private ResponseEntity<List<Room>> allRooms(@CurrentUser User user) {
        return new ResponseEntity<List<Room>>(this.hotelService.findByUser(user).getRoomList(), HttpStatus.OK);
    }

}
