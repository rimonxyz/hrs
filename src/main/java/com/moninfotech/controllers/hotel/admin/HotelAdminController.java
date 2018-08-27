package com.moninfotech.controllers.hotel.admin;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SortAttributes;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.commons.utils.DateUtils;
import com.moninfotech.commons.utils.FileIO;
import com.moninfotech.commons.utils.PasswordUtil;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.exceptions.NotFoundException;
import com.moninfotech.exceptions.invalid.InvalidException;
import com.moninfotech.exceptions.nullexceptions.NullPasswordException;
import com.moninfotech.logger.Log;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Controller
@RequestMapping("/admin/hotels")
@Secured("ROLE_ADMIN")
public class HotelAdminController {

    private final HotelService hotelService;
    private final UserService userService;
    private final RoomService roomService;
    private final CategoryService categoryService;

    @Autowired
    public HotelAdminController(HotelService hotelService, UserService userService, RoomService roomService, CategoryService categoryService) {
        this.hotelService = hotelService;
        this.userService = userService;
        this.roomService = roomService;
        this.categoryService = categoryService;
    }

    // Get All Hotels paginated
    @RequestMapping(value = "", method = RequestMethod.GET)
    private String all(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(value = "sortBy", required = false, defaultValue = SortAttributes.FIELD_ID) String soryBy,
                       @RequestParam(value = "type", required = false, defaultValue = Hotel.Type.BOTH) String type,
                       @RequestParam(value = "desc", required = false, defaultValue = "true") boolean isDesc,
                       @RequestParam(value = "filterType", required = false) String filterType,
                       @RequestParam(value = "filterValue", required = false) String filterValue,
                       Model model) {
        if (page <= 0) page = 0;
        List<Hotel> hotelList = hotelService.findAll(page, SortAttributes.Page.SIZE, soryBy, isDesc);
        if (filterType != null && !filterType.isEmpty() && filterValue != null && !filterValue.isEmpty())
            hotelList = this.hotelService.filterHotels(hotelList, filterType, filterValue);

        // Filter by type
        hotelList = this.hotelService.filterHotels(hotelList, type);

        model.addAttribute(hotelList);
        model.addAttribute("hotelType", type);
        model.addAttribute("page", page);
//        model.addAttribute("template", "fragments/hotel/admin/all");
        return "adminlte/fragments/hotel/admin/all";
    }


    // Create a hotel by Admin
    // @GET
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    private String createPage() {
        return "hotel/admin/create";
    }

    //@POST
    @Transactional
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    String create(@ModelAttribute Hotel hotel, BindingResult bindingResult,
                  @RequestParam(value = "userId", required = false) Long userId,
                  @RequestParam("images") MultipartFile[] multipartFiles) throws Exception, NullPasswordException, InvalidException {
        if (bindingResult.hasErrors())
            System.out.print("Binding ERROR: " + bindingResult.toString());
        // set image to the hotel entity if it's valid.
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        // if all images aren't valid
        if (FileIO.isNotEmpty(multipartFiles)) { // if images one or more images are choosen to be uploaded
            if (files.size() != multipartFiles.length)
                return "redirect:/admin/hotels?message=One or more images are invalid.";
            hotel.setImages(files);
        }
        // first save user
        User user;
        if (userId != null) { // hotel updating
            user = this.userService.findOne(userId);
            // save previous created date when updating
            if (hotel.getId() != null) {
                Hotel existingHotel = this.hotelService.findOne(hotel.getId());
                if (existingHotel != null) {
                    hotel.setCreated(existingHotel.getCreated());
                    if (hotel.getImages() == null || hotel.getImages().isEmpty())
                        hotel.setImages(existingHotel.getImages());
                }
            }
        } else {// hotel create -new
            user = hotel.getUser();
            user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), PasswordUtil.EncType.BCRYPT_ENCODER, null));
        }

        if (user == null) return "redirect:/admin/hotels?message=Can\'t update hotel!";
        user = this.userService.save(user);
        // save hotel address as user address by default
        user.setAddress(hotel.getAddress());
        // set default role for this user. in this case :ROLE_HOTEL
        List<String> roles = new ArrayList<>();
        roles.add(Constants.Roles.ROLE_HOTEL_ADMIN);
        user.setRoles(roles);
        // set saved user (with persisted id) to hotel
        hotel.setUser(user);
        // then save hotel
        hotel = this.hotelService.save(hotel);
        return "redirect:/admin/hotels?messageinfo=" + hotel.getName() + " is saved. A confirmation link is sent to the hotel admin\'s email address. Please ask them to confirm their account by clicking the link on that email. Or alternatively you can enable this user by clicking \'enable\' button.";
    }

    // Edit hotel informations by admin
    //@GET
//    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
//    private String editPage(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("hotel", hotelService.findOne(id));
//        return "hotel/admin/edit";
//    }
//
//    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
//    private String edit(@ModelAttribute Hotel hotel, BindingResult bindingResult,
//                        @PathVariable("id") Long id,
//                        @RequestParam("images") MultipartFile[] multipartFiles) throws IOException {
//        if (bindingResult.hasErrors())
//            System.out.println(bindingResult.toString());
//        // set existing id to update
//        hotel.setId(id);
//
//        Hotel existingHotel = this.hotelService.findOne(id);
//        if (existingHotel != null) {
//            // set created date from previous entity
//            hotel.setCreated(existingHotel.getCreated());
//            // set previous user to hotel entity
//            hotel.setUser(existingHotel.getUser());
//            // if no image is uploaded the set previous image if available
//            if (multipartFiles == null || multipartFiles.length == 0) hotel.setImages(existingHotel.getImages());
//        }
//
//        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
//        // if all images aren't valid
//        if (FileIO.isNotEmpty(multipartFiles)) { // if images one or more images are choosen to be uploaded
//            if (files.size() != multipartFiles.length)
//                return "redirect:/admin/hotels/edit/" + id + "?message=One or more images are invalid!";
//            hotel.setImages(files);
//        }
//        hotel = this.hotelService.save(hotel);
//        return "redirect:/admin/hotels?messageinfo=" + hotel.getName() + " informations are updated!";
//    }

    // Delete entity
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    private String delete(@PathVariable Long id) {
        this.hotelService.delete(id);
        return "redirect:/admin/hotels?message=Successfully deleted!";
    }

    @PostMapping("/softdelete/{hotelId}")
    private String softDelete(@PathVariable("hotelId") Long hotelId) throws NotFoundException, InvalidException {
        this.hotelService.softDelete(hotelId);
        return "redirect:/admin/hotels?message=Successfully deleted!";
    }
//    // returns image with that entity id
//    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
//    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
//        return new ResponseEntity<byte[]>(this.hotelService.findOne(id).getImage(), HttpStatus.OK);
//    }

    // disable user of a hotel
    @RequestMapping(value = "/{id}/action", method = RequestMethod.POST)
    private String disable(@PathVariable("id") Long id, @RequestParam("enabled") Boolean enabled) throws InvalidException {
        Hotel hotel = this.hotelService.findOne(id);
        if (hotel == null || hotel.getUser() == null) return "redirect:/admin/hotels?message=User can not be found!";
        hotel.getUser().setEnabled(enabled);
        hotel = this.hotelService.save(hotel);
        return "redirect:/admin/hotels?messageinfo=" + hotel.getName() + " updated!";
    }

    // MANAGE ROOMS FOR ADMIN

    // get all room
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private String allRooms(@PathVariable("id") Long id,
                            @RequestParam(value = "filterType", required = false) String filterType,
                            @RequestParam(value = "value", required = false) String value, Model model) {
        if (filterType == null || filterType.isEmpty() || value == null || value.isEmpty()) {
            filterType = FilterType.DATE;
            value = DateUtils.getParsableDateFormat().format(new Date());
        }
        Hotel hotel = this.hotelService.findOne(id);
        if (hotel == null) return "redirect:/?message=You are not authorized to perform this action!";
        List<Room> roomList = hotel.getRoomList();
        List<Long> bookedIds = this.roomService.filterRoomIds(roomList, filterType, value);

        model.addAttribute("hotel", hotel);
        model.addAttribute("roomList", roomList);
        model.addAttribute("categoryList", this.categoryService.findAll());
        model.addAttribute("bookedIds", bookedIds);
        model.addAttribute("filterType", filterType);
        model.addAttribute("filterValue", value);

//        model.addAttribute("template", "fragments/room/admin/all");
        return "adminlte/fragments/room/admin/all";
    }

    // search
    @RequestMapping(value = "/{id}/search", method = RequestMethod.GET)
    private String searchRoom(@PathVariable("id") Long id,
                              @RequestParam("q") String query,
                              Model model) {
        Hotel hotel = this.hotelService.findOne(id);
        List<Room> roomList = this.roomService.searchRooms(hotel, query);
        if (roomList == null)
            return "hotel/admin/allRooms?message=One or more rooms can not be found!";
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomList", roomList);
        model.addAttribute("categoryList", this.categoryService.findAll());
        return "hotel/admin/allRooms";
    }

    // create
    @RequestMapping(value = "/{id}/create", method = RequestMethod.GET)
    private String createPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("hotel", this.hotelService.findOne(id));
        model.addAttribute("categoryList", this.categoryService.findAll());
        return "hotel/admin/createRoom";
    }

    @RequestMapping(value = "/{hotelId}/create", method = RequestMethod.POST)
    private String create(@ModelAttribute Room room, BindingResult bindingResult,
                          @PathVariable("hotelId") Long id,
                          @RequestParam("images") MultipartFile[] multipartFiles) {
        System.out.println(room.toString());
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        // if all images aren't valid
        if (files.size() != multipartFiles.length) return "redirect:/admin/hotels";
        room.setImages(files);
        Hotel hotel = this.hotelService.findOne(id);
        if (hotel == null) return "redirect:/admin/hotels?message=Hotel not found!";
        room.setHotel(hotel);
        // check if room category was saved previously, if not then save and set to room
//        if (room.getCategory().getId() == null)
//            room.setCategory(this.categoryService.save(room.getCategory()));
//        else // else find category and set to room
        room.setCategory(this.categoryService.findOne(room.getCategory().getId()));
        room = this.roomService.save(room);
//        System.out.println(room);
        return "redirect:/admin/hotels/" + id + "?messageinfo=Successfully added room " + room.getRoomNumber();
    }

    // UPDATE ROOM
    @RequestMapping(value = "/{hotelId}/edit/{roomId}", method = RequestMethod.GET)
    private String editRoomPage(@PathVariable("hotelId") Long hotelId,
                                @PathVariable("roomId") Long roomId, Model model) {
        Hotel hotel = this.hotelService.findOne(hotelId);
        if (hotel == null) return "redirect:/admin/hotels/" + hotelId + "?message=Hotel not found!";
        Room room = this.roomService.findOne(roomId);
        if (room == null) return "redirect:/admin/hotels/" + hotelId + "?message=Room not found!";
        model.addAttribute("categoryList", this.categoryService.findAll());
        model.addAttribute("hotel", hotel);
        model.addAttribute("room", room);
        return "hotel/admin/editRoom";
    }

    @RequestMapping(value = "/{hotelId}/edit/{roomId}", method = RequestMethod.POST)
    private String editRoom(@ModelAttribute Room room, BindingResult bindingResult,
                            @PathVariable("hotelId") Long hotelId,
                            @PathVariable("roomId") Long roomId,
                            @RequestParam("images") MultipartFile[] multipartFiles) {
        if (bindingResult.hasErrors()) Log.print(bindingResult.toString());
        if (room == null) return "redirect:/admin/hotels/" + hotelId + "?message=Room not found!";
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        // if all images aren't valid
        boolean isImagesValid = files.size() == multipartFiles.length;
        String message = "";
        if (!isImagesValid) {
            // set previous images
            room.setImages(this.roomService.findOne(roomId).getImages());
            message = "Successfully updated room, but images were invalid, so I updated with previous images.";
        } else
            room.setImages(files);
        room.setId(roomId);
        Hotel hotel = this.hotelService.findOne(hotelId);
        if (hotel == null) return "redirect:/admin/hotels/" + hotelId + "?message=Hotel not found!";
        room.setHotel(hotel);
        room.setCategory(this.categoryService.findOne(room.getCategory().getId()));
        room = this.roomService.save(room);
        if (isImagesValid)
            message = "Successfully updated room " + room.getRoomNumber();
        return "redirect:/admin/hotels/" + hotelId + "?message=" + message;
    }

//    // DELETE ROOM
//    @RequestMapping(value = "/{hotelId}/delete/{roomId}", method = RequestMethod.POST)
//    private String deleteRoom(@PathVariable("roomId") Long roomId) {
//
//        this.roomService.delete(roomId);
//        return "redirect:/hotel/rooms?message=Deleted!";
//    }

}
