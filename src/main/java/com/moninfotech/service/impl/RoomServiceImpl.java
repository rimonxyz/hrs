package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.Utils;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.commons.utils.DateUtils;
import com.moninfotech.commons.utils.FileIO;
import com.moninfotech.domain.*;
import com.moninfotech.exceptions.forbidden.ForbiddenException;
import com.moninfotech.exceptions.invalid.InvalidException;
import com.moninfotech.repository.RoomRepository;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.HotelService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 4/4/17.
 */
@Service
public class RoomServiceImpl implements RoomService {

    private final HotelService hotelService;
    private final RoomRepository roomRepo;
    private final CategoryService categoryService;

    @Autowired
    public RoomServiceImpl(HotelService hotelService, RoomRepository roomRepo, CategoryService categoryService) {
        this.hotelService = hotelService;
        this.roomRepo = roomRepo;
        this.categoryService = categoryService;
    }

    @Override
    public Room save(Room room) {
        return this.roomRepo.save(room);
    }


    @Override
    public Room findOne(Long id) {
        return this.roomRepo.findOne(id);
    }

    @Override
    public List<Room> findAll(int page, int size) {
        return this.roomRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public List<Room> findByHotel(Long hotelId) {
        return this.roomRepo.findByHotelIdOrderByRoomNumberAsc(hotelId);
    }

    @Override
    public void delete(Long id) {
        this.roomRepo.delete(id);
    }

    @Override
    public List<Room> findAll(Long[] ids) {
        return this.roomRepo.findAll(Arrays.asList(ids));
    }

    @Override
    public List<Long> filterRoomIds(List<Room> roomList, String filterType, String value) {
        if (filterType.equals(FilterType.DATE))
            return filterRoomIdsByDate(roomList, value);
        else if (filterType.equals(FilterType.CATEGORY))
            return filterRoomIdsByCategory(roomList, value);
        return null;
    }

    @Override
    public List<Room> searchRooms(Hotel hotel, String query) {
        return this.roomRepo.findByHotelAndRoomNumberContainingIgnoreCase(hotel, query);
    }

    @Override
    public List<Room> organiseRoomListCategory(List<Room> roomList, Category newCategory) {
        List<Room> newList = new ArrayList<>();
        roomList.forEach(room -> {
            room.setCategory(newCategory);
            newList.add(room);
        });
        roomList.clear();
        return newList;
    }

    @Override
    public List<Room> saveAll(List<Room> processedRoomList) {
        return this.roomRepo.save(processedRoomList);
    }

    @Override
    public List<Room> removeRoom(List<Room> roomList, Long roomId) {
        if (roomList == null) return null;
        return roomList.stream()
                .filter(room -> !room.getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByHotelAndCategory(Hotel hotel, Category category) {
        return this.roomRepo.findByHotelAndCategory(hotel, category);
    }

    @Override
    public List<Room> updateDiscounts(List<Room> roomList, Map<Date, Integer> discountMap) {
        List<Room> rooms = new ArrayList<>();
        roomList
                .forEach(room -> {
                    room.setDiscountMap(new HashMap<>(discountMap));
                    rooms.add(room);
                });
        return rooms;
    }

    @Override
    public List<Room> save(List<Room> roomList) {
        return this.roomRepo.save(roomList);
    }

    @Override
    public List<Room> filterRoomList(List<Room> roomList, String filterType, String filterValue) throws ParseException {
        if (roomList == null || roomList.isEmpty() || filterType == null || filterType.isEmpty() || filterValue == null || filterValue.isEmpty())
            return roomList;

        switch (filterType) {
            case FilterType.HOTEL_NAME:
                return roomList.stream()
                        .filter(room -> room.getHotel().getName().toLowerCase().contains(filterValue.toLowerCase()))
                        .collect(Collectors.toList());
            case FilterType.HOTEL_TYPE:
                return roomList.stream()
                        .filter(room -> room.getHotel().getType().toLowerCase().equals(filterValue.toLowerCase()))
                        .collect(Collectors.toList());
        }
        return roomList;
    }

    @Override
    public List<Room> filterByCategory(List<Room> roomList, String categoryName) {
        if (roomList == null)
            throw new IllegalArgumentException("Room List can not be null: filterByCategory(List<Room> roomList, String categoryName)");
        return roomList.stream()
                .filter(room -> room.getCategory().getName().equals(categoryName)).collect(Collectors.toList());
    }

    @Override
    public Room create(Room room, Long hotelId, MultipartFile[] multipartFiles, User user) throws InvalidException, ForbiddenException {
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        // if all images aren't valid
        if (FileIO.isNotEmpty(multipartFiles)) { // if images one or more images are choosen to be uploaded
            if (files.size() != multipartFiles.length)
                throw new InvalidException("/hotel/rooms/create", "One or more images are invalid.");
            room.setImages(files);
        }
        Hotel hotel = null;
        if (hotelId == null)
            hotel = this.hotelService.findByUser(user);
        else
            hotel = this.hotelService.findOne(hotelId);

        if (hotel == null) throw new ForbiddenException("/", "You are not authorized to do this action.");
        room.setHotel(hotel);
        if (Utils.NULL_OR_EMPTY(room.getRoomNumber())) room.setRoomNumber(".");
        // Room Fascilities
        if (room.getFacilities() == null) room.setFacilities(new Facilities());
        // check if room category was saved previously, if not then save and set to room
//        if (room.getCategory().getId() == null)
//            room.setCategory(this.categoryService.save(room.getCategory()));
//        else // else find category and set to room
        room.setCategory(this.categoryService.findOne(room.getCategory().getId()));
        return this.roomRepo.save(room);
    }

    private List<Long> filterRoomIdsByCategory(List<Room> roomList, String value) {
        List<Long> idsList = new ArrayList<>();
        roomList.forEach(room -> {
            if (room.getCategory().getName().equals(value))
                idsList.add(room.getId());
        });
        return idsList;
    }

    public List<Long> filterRoomIdsByDate(List<Room> roomList, String value) {
        SimpleDateFormat sdf = DateUtils.getParsableDateFormat();
        List<Long> idList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        roomList.forEach(room -> {
            try {
                if (room.isBooked(sdf.parse(value))) {
                    idList.add(room.getId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return idList;
    }

}
