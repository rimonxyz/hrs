package com.moninfotech.controllers.hotel;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 4/16/17.
 */
@Controller
@RequestMapping(value = "/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final RoomService roomService;
    private final CategoryService categoryService;

    @Autowired
    public HotelController(HotelService hotelService, RoomService roomService, CategoryService categoryService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.categoryService = categoryService;
    }

    // Get All Hotels paginated
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String all(@RequestParam(value = "query", required = false) String query,
                      @RequestParam(value = "type", required = false, defaultValue = Hotel.Type.BOTH) String type,
                      @RequestParam(value = "page", required = false) Integer page,
                      @RequestParam(value = "sortBy", required = false) String sortBy,
                      @RequestParam(value = "isDesc", required = false) boolean isDesc,
                      @RequestParam(value = "filterType", required = false) String filterType,
                      @RequestParam(value = "filterValue", required = false) String filterValue,
                      Model model) {
        if (page == null || page < 0) page = 0;
        List<Hotel> hotelList;
        if (query != null && !query.isEmpty()) {
            // if search query not null filter hotels
            hotelList = this.hotelService.findByAddressArea(query, page);
            hotelList.addAll(this.hotelService.findByAddressUpazila(query, page));
            if (hotelList.isEmpty())
                hotelList = this.hotelService.findByNameContaining(query, page);
        } else {
            // else find all hotel
            hotelList = hotelService.findAll(page, 10, sortBy, isDesc);
        }
        // filter hotels if filter exists
        if (filterType != null && !filterType.isEmpty() && filterValue != null && !filterValue.isEmpty())
            hotelList = this.hotelService.filterHotels(hotelList, filterType, filterValue);

        // filter by type
        hotelList = this.hotelService.filterHotels(hotelList, type);

        model.addAttribute("isDesc", !isDesc);
        if (filterValue != null)
            model.addAttribute("filterValue", filterValue);
        if (query != null)
            model.addAttribute("query", query);
        model.addAttribute(hotelList);
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("hotelType", type);
        model.addAttribute("page", page);
//        model.addAttribute("template", "fragments/hotel/all");
        return "adminlte/fragments/hotel/all";
    }

    // get all room
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private String allRooms(@PathVariable("id") Long id,
                            @RequestParam(value = "q", required = false) String query,
                            @RequestParam(value = "filterType", required = false) String filterType,
                            @RequestParam(value = "value", required = false) String value,
                            @RequestParam(value = "categoryFilter", required = false, defaultValue = "") String categoryFilter, Model model) {

        if (filterType == null || filterType.isEmpty() || value == null || value.isEmpty()) {
            filterType = FilterType.DATE;
            value = DateUtils.getParsableDateFormat().format(new Date());
        }
        Hotel hotel = this.hotelService.findOne(id);
        if (hotel == null) return "redirect:/?message=You are not authorized to perform this action!";

        // Load rooms, all if search query is empty.
        List<Room> roomList;
        if (query == null) {
            roomList = hotel.getRoomList();
            if (!categoryFilter.isEmpty() && !categoryFilter.toLowerCase().equals("all"))
                roomList = roomService.filterByCategory(roomList, categoryFilter);
        } else
            roomList = this.roomService.searchRooms(hotel, query);
        List<Long> bookedIds = this.roomService.filterRoomIds(roomList, filterType, value);

        model.addAttribute("hotel", hotel);
        model.addAttribute("roomList", roomList);
        model.addAttribute("categoryList", this.categoryService.findAll());
        model.addAttribute("bookedIds", bookedIds);

        model.addAttribute("filterType", filterType);
        model.addAttribute("filterValue", value);

        model.addAttribute("categoryFilter", categoryFilter);

        model.addAttribute("sidebarCollapse", true);
//        model.addAttribute("template", "fragments/hotel/details");
        return "adminlte/fragments/hotel/details";
    }

//    // returns image with that entity id
//    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
//    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
//        return new ResponseEntity<byte[]>(this.hotelService.findOne(id).getImage(), HttpStatus.OK);
//    }

    @RequestMapping(value = "{hotelId}/image/{imageNumber}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImages(@PathVariable("hotelId") Long hotelId,
                                             @PathVariable("imageNumber") Integer imageNumber) {
        Hotel hotel = this.hotelService.findOne(hotelId);
        List<byte[]> images = null;
        // set images from room
        if (hotel.getImages() != null && !hotel.getImages().isEmpty())
            images = hotel.getImages();

        if (images != null && images.size() > imageNumber) {
            return new ResponseEntity<>(images.get(imageNumber), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
