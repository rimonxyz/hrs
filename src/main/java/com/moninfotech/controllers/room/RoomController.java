package com.moninfotech.controllers.room;

import com.moninfotech.domain.Room;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by sayemkcn on 5/18/17.
 */
@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping(value = "{roomId}/image/{imageNumber}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImages(@PathVariable("roomId") Long roomId,
                                             @PathVariable("imageNumber") Integer imageNumber) {
        Room room = this.roomService.findOne(roomId);
        List<byte[]> images;
        // set images from room
        if (room.getImages() != null && !room.getImages().isEmpty())
            images = room.getCategory().getImages();
        else images = room.getCategory().getImages(); // set category images if room has no image
        if (images != null && images.size() > imageNumber) {
            return new ResponseEntity<>(images.get(imageNumber), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
