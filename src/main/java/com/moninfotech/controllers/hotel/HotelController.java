package com.moninfotech.controllers.hotel;

import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.commons.pojo.ParamFacilities;
import com.moninfotech.commons.utils.DateUtils;
import com.moninfotech.commons.utils.FileIO;
import com.moninfotech.domain.Category;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.exceptions.invalid.InvalidException;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/filter")
    private String filterHotels(@RequestParam(value = "query", defaultValue = "") String query,
                                @RequestParam("star") String star,
                                @RequestParam("price") String price,
                                @RequestParam("accomodationType") String accomodationType,
                                @RequestParam("rating") Integer rating,
                                @ModelAttribute ParamFacilities facilities,
                                @RequestParam Map<String,String> fMap,
                                @RequestParam(value = "type", defaultValue = "Hotel") String hotelType,
                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value = "sortBy", required = false) String sortBy,
                                @RequestParam(value = "isDesc", required = false) boolean isDesc,
                                Model model) {
        List<Hotel> hotels = this.hotelService.filter(query, star, price, accomodationType, rating == null ? 0 : rating, facilities);

        model.addAttribute("hotelList", hotels);
        model.addAttribute("isDesc", !isDesc);

        model.addAttribute("query", query);
        model.addAttribute("price", price);
        model.addAttribute("star", star);
        model.addAttribute("accomodationType", accomodationType);
        model.addAttribute("rating", rating);
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("hotelType", hotelType);
        model.addAttribute("f",facilities);
        model.addAttribute("page", page);
        model.addAttribute("sidebarCollapse", true);
//        model.addAttribute("template", "fragments/hotel/all");
        return "adminlte/fragments/hotel/all";
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
            hotelList = this.hotelService.searchHotel(query, page);
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
        if (query == null) query = "";
        model.addAttribute("query", query);
        model.addAttribute(hotelList);
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("hotelType", type);
        model.addAttribute("page", page);
        model.addAttribute("f", new ParamFacilities());
        model.addAttribute("sidebarCollapse", true);
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
            roomList = this.roomService.findByHotel(hotel.getId());
            if (!categoryFilter.isEmpty() && !categoryFilter.toLowerCase().equals("all"))
                roomList = roomService.filterByCategory(roomList, categoryFilter);
        } else
            roomList = this.roomService.searchRooms(hotel, query);
        List<Long> bookedIds = this.roomService.filterRoomIds(roomList, filterType, value);

        model.addAttribute("checkInDate",DateUtils.getParsableDateFormat().format(new Date()));
        model.addAttribute("checkoutDate",DateUtils.getParsableDateFormat().format(DateUtils.getNextDay(new Date())));
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomList", roomList);
        model.addAttribute("categoryList", hotel.getEffectiveCategories());
        model.addAttribute("bookedIds", bookedIds);

        model.addAttribute("filterType", filterType);
        model.addAttribute("filterValue", value);

        model.addAttribute("categoryFilter", categoryFilter);

        model.addAttribute("sidebarCollapse", true);
//        model.addAttribute("template", "fragments/hotel/details");
        return "adminlte/fragments/hotel/details";
    }

    @GetMapping("/{id}/checkAvailablity")
    private String checkAvailablity(@PathVariable("id") Long hotelId,
                                    @RequestParam("categoryId") String category,
                                    @RequestParam("checkInDate") Date checkInDate,
                                    @RequestParam("checkoutDate") Date checkoutDate,
                                    Model model) throws InvalidException {
        Long categoryId;
        Category cat = null;
        if (category == null || category.toLowerCase().equals("all")) categoryId = null;
        else {
            categoryId = Long.parseLong(category);
            cat = this.categoryService.findOne(categoryId);
        }
        List<Room> filteredRooms = this.roomService.filter(hotelId,checkInDate,checkoutDate,categoryId);
        Hotel hotel = this.hotelService.findOne(hotelId);

        model.addAttribute("checkInDate",DateUtils.getParsableDateFormat().format(checkInDate));
        model.addAttribute("checkoutDate",DateUtils.getParsableDateFormat().format(checkoutDate));
        if (cat != null)
            model.addAttribute("categoryFilter", cat.getName());
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomList", filteredRooms);
        model.addAttribute("categoryList", hotel.getEffectiveCategories());
//        model.addAttribute("bookedIds", bookedIds);
        model.addAttribute("sidebarCollapse", true);
        return "adminlte/fragments/hotel/details";
    }
//    // returns image with that entity id
//    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
//    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
//        return new ResponseEntity<byte[]>(this.hotelService.findOne(id).getImage(), HttpStatus.OK);
//    }

    @RequestMapping(value = "{hotelId}/image/{imageNumber}", method = RequestMethod.GET)
    private ResponseEntity<byte[]> getImages(@PathVariable("hotelId") Long hotelId,
                                             @PathVariable("imageNumber") Integer imageNumber,
                                             @RequestParam(value = "height", required = false) Integer height,
                                             @RequestParam(value = "width", required = false) Integer width) throws IOException {
        Hotel hotel = this.hotelService.findOne(hotelId);
        if (hotel == null) return ResponseEntity.noContent().build();
        List<byte[]> images = hotel.getImages();

        if (images != null && images.size() > imageNumber) {
            byte[] imageBytes = images.get(imageNumber);
            if (height != null && width != null)
                imageBytes = FileIO.getScaledImage(imageBytes, width, height);

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
