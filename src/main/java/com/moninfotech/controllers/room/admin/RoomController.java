package com.moninfotech.controllers.room.admin;

import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
@Controller
@RequestMapping("/hotel/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private CategoryService categoryService;

    // create
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    private String createPage(Model model) {
        model.addAttribute("categoryList", this.categoryService.findAll());
        return "room/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    private String create(@ModelAttribute Room room, BindingResult bindingResult,
                          @RequestParam("images") MultipartFile[] multipartFiles,
                          @CurrentUser User user) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        List<byte[]> files = this.roomService.convertMultipartFiles(multipartFiles);
        if (files.size() != multipartFiles.length) return "redirect:/hotel/rooms/create";
        room.setImages(files);
        room.setHotel(this.hotelService.findByUser(user));
        System.out.println(room.getCategory().getId());
        // check if room category was saved previously, if not then save and set to room
//        if (room.getCategory().getId() == null)
//            room.setCategory(this.categoryService.save(room.getCategory()));
//        else // else find category and set to room
        room.setCategory(this.categoryService.findOne(room.getCategory().getId()));
        room = this.roomService.save(room);
//        System.out.println(room);
        return "redirect:/hotel/rooms?message=Successfully added room.";
    }

}
