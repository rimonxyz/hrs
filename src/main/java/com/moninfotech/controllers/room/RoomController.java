package com.moninfotech.controllers.room;

import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.commons.utils.DateUtils;
import com.moninfotech.domain.Room;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 5/18/17.
 */
@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final CategoryService categoryService;

    @Autowired
    public RoomController(RoomService roomService,CategoryService categoryService) {
        this.roomService = roomService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/{id}/{checkInDate}/{checkoutDate}")
    private String details(@PathVariable("id") Long id,
                           @PathVariable(value = "checkInDate",required = false) String checkInDate,
                           @PathVariable(value = "checkoutDate",required = false) String checkoutDate,
                           Model model) {
//        System.out.println("dateStr "+dateStr);
        if (!DateUtils.isParsable(checkInDate))
            checkInDate = DateUtils.getParsableDateFormat().format(new Date());
        if (!DateUtils.isParsable(checkoutDate))
            checkoutDate= DateUtils.getParsableDateFormat().format(new Date());
        Room room = this.roomService.findOne(id);
        model.addAttribute("room", room);
        model.addAttribute("hotel",room.getHotel());

        model.addAttribute("checkInDate",checkInDate);
        model.addAttribute("checkoutDate",checkoutDate);
        model.addAttribute("filterType", FilterType.DATE);
        model.addAttribute("categoryList",this.categoryService.findAll());

//        model.addAttribute("template", "fragments/room/details");
        return "adminlte/fragments/room/details";
    }


    @RequestMapping(value = "{roomId}/image/{imageNumber}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImages(@PathVariable("roomId") Long roomId,
                                             @PathVariable("imageNumber") Integer imageNumber) {
        Room room = this.roomService.findOne(roomId);
        List<byte[]> images;
        // set images from room
        if (room.getImages() != null && !room.getImages().isEmpty())
            images = room.getImages();
        else images = room.getCategory().getImages(); // set category images if room has no image
        if (images != null && images.size() > imageNumber)
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(images.get(imageNumber));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
