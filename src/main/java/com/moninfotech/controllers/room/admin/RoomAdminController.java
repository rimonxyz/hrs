package com.moninfotech.controllers.room.admin;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.commons.FileIO;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.domain.annotations.CurrentUser;
import com.moninfotech.logger.Log;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
@Controller
@RequestMapping("/hotel/rooms")
public class RoomAdminController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private CategoryService categoryService;

    // get all room
    @RequestMapping(value = "", method = RequestMethod.GET)
    private String allRooms(@CurrentUser User user,
                            @RequestParam(value = "filterType", required = false) String filterType,
                            @RequestParam(value = "value", required = false) String value, Model model) {
        if (filterType == null || filterType.isEmpty() || value == null || value.isEmpty()) {
            filterType = FilterType.DATE;
            value = DateUtils.getParsableDateFormat().format(new Date());
        }

        Hotel hotel = this.hotelService.findByUser(user);
        if (hotel == null) return "redirect:/?message=You are not authorized to perform this action!";
        List<Room> roomList = hotel.getRoomList();
        List<Long> bookedIds = this.roomService.filterRoomIds(roomList, filterType, value);

        model.addAttribute("hotel",hotel);
        model.addAttribute("roomList", roomList);
        model.addAttribute("categoryList", this.categoryService.findAll());
        model.addAttribute("bookedIds", bookedIds);
        model.addAttribute("filterValue", value);
        return "room/admin/all";
    }

    // search
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    private String searchRoom(@CurrentUser User user,
                              @RequestParam("q") String query,
                              Model model) {
        if (user == null) return "redirect:/login";
        Hotel hotel = this.hotelService.findByUser(user);
        List<Room> roomList = this.roomService.searchRooms(hotel, query);
        if (roomList == null)
            return "room/admin/all?message=One or more rooms can not be found!";
        model.addAttribute("roomList", roomList);
        model.addAttribute("categoryList", this.categoryService.findAll());
        return "room/admin/all";
    }

    // create
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    private String createPage(Model model) {
        model.addAttribute("categoryList", this.categoryService.findAll());
        return "room/admin/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    private String create(@ModelAttribute Room room, BindingResult bindingResult,
                          @RequestParam("images") MultipartFile[] multipartFiles,
                          @CurrentUser User user) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        // if all images aren't valid
        if (FileIO.isNotEmpty(multipartFiles)) { // if images one or more images are choosen to be uploaded
            if (files.size() != multipartFiles.length)
                return "redirect:/hotel/rooms/create?message=One or more images are invalid.";
            room.setImages(files);
        }
        Hotel hotel = this.hotelService.findByUser(user);
        if (hotel == null) return "redirect:/?message=You are not authorized to do this action.";
        room.setHotel(hotel);
        // check if room category was saved previously, if not then save and set to room
//        if (room.getCategory().getId() == null)
//            room.setCategory(this.categoryService.save(room.getCategory()));
//        else // else find category and set to room
        room.setCategory(this.categoryService.findOne(room.getCategory().getId()));
        room = this.roomService.save(room);
//        System.out.println(room);
        return "redirect:/hotel/rooms?message=Successfully added room.";
    }

    // Update
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    private String editPage(@PathVariable("id") Long id, Model model) {
        Room room = this.roomService.findOne(id);
        if (room == null) return "redirect:/hotel/rooms?message=Room not found!";
        model.addAttribute("categoryList", this.categoryService.findAll());
        model.addAttribute("room", room);
        return "room/admin/edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    private String edit(@ModelAttribute Room room, BindingResult bindingResult,
                        @PathVariable("id") Long id,
                        @RequestParam("images") MultipartFile[] multipartFiles,
                        @CurrentUser User user) {
        if (bindingResult.hasErrors()) Log.print(bindingResult.toString());
        if (room == null) return "redirect:/hotel/rooms?message=Room not found!";
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        // if all images aren't valid
        boolean isImagesValid = files.size() == multipartFiles.length;
        String message = "";
        if (!isImagesValid) {
            // set previous images
            room.setImages(this.roomService.findOne(id).getImages());
            message = "Successfully updated room, but images were invalid, so I updated with previous images.";
        } else
            room.setImages(files);
        room.setId(id);
        Hotel hotel = this.hotelService.findByUser(user);
        if (hotel == null) return "redirect:/?message=You are not authorized to do this action.";
        room.setHotel(hotel);
        room.setCategory(this.categoryService.findOne(room.getCategory().getId()));
        room = this.roomService.save(room);
        if (isImagesValid)
            message = "Successfully updated room " + room.getRoomNumber();
        return "redirect:/hotel/rooms?message=" + message;
    }

//    // delete
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//    private String delete(@PathVariable("id") Long id) {
//        this.roomService.delete(id);
//        return "redirect:/hotel/rooms?message=Deleted!";
//    }
}
