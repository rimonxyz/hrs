package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.DateUtils;
import com.moninfotech.commons.SortAttributes;
import com.moninfotech.commons.Utils;
import com.moninfotech.domain.*;
import com.moninfotech.repository.BookingRepository;
import com.moninfotech.service.BookingService;
import com.moninfotech.service.HotelService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 4/18/17.
 */
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepo;
    private final HotelService hotelService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepo, HotelService hotelService) {
        this.bookingRepo = bookingRepo;
        this.hotelService = hotelService;
    }

    @Override
    public List<Booking> findByUser(User user, Integer page, Integer size) {
        if (page == null || size == null)
            return this.bookingRepo.findByUser(user);
        return this.bookingRepo.findByUser(user, new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public List<Booking> findByHotel(Hotel hotel, Integer page, Integer size) {
        if (page == null || size == null)
            return this.bookingRepo.findByHotel(hotel);
        return this.bookingRepo.findByHotel(hotel, new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public Booking findOne(Long id) {
        return this.bookingRepo.findOne(id);
    }

    @Override
    public List<Booking> findAll(Integer page, Integer size) {
        if (page == null || size == null)
            return this.bookingRepo.findAll();
        return this.bookingRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public boolean belongsTo(Booking booking, User user) {
        // If this booking doesn't belong to this logged in user
        if (!booking.getUser().getId().equals(user.getId())) {
            // check if logged in user is hotel admin.
            if (user.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
                // check if this booking isn't belongs to his/her hotel, then restrict and send redirect with message
                Hotel hotel = this.hotelService.findByUser(user);
                if (!booking.getHotel().getId().equals(hotel.getId()))
                    return false;
            }
        }
        return true;
    }

    @Override
    public Booking save(Booking booking) {
        return this.bookingRepo.save(booking);
    }

    @Override
    public synchronized Long[] convertToIds(String json) {
        try {
            Long[] ids = null;
            // data json
            JSONObject jsonObject = new JSONObject(json);
            //id array json
            String idsString = jsonObject.getString("ids");
            if (!idsString.isEmpty()) {
                String[] splittedIds = idsString.split(",");
                ids = new Long[splittedIds.length];
                for (int i = 0; i < splittedIds.length; i++) {
                    ids[i] = Long.parseLong(splittedIds[i]);
                }

            }
            return ids;
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    @Override
    public Date[] getDates(String json) {
        try {
            // data json
            JSONObject jsonObject = new JSONObject(json);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date[] dates = new Date[2];
            dates[0] = sdf.parse(jsonObject.getString("startDate"));
            dates[1] = sdf.parse(jsonObject.getString("endDate"));
            return dates;
        } catch (JSONException e) {
            throw new JSONException(e);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        } catch (ParseException e) {
            System.out.println("getDates(String json) :" + e.toString());
        }
        return new Date[2];
    }

    @Override
    public List<Booking> findAll(Long[] ids) {
        return this.bookingRepo.findAll(Arrays.asList(ids));
    }

    @Override
    public Collection<Booking> getBookingList(Hotel hotel, User user) {
        List<Booking> myBookingList = new ArrayList<>();
        hotel.getBookingList().forEach(booking -> {
            if (booking.getUser().getId().equals(user.getId()))
                myBookingList.add(booking);
        });
        myBookingList.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        return myBookingList;
    }

    @Override
    public boolean isDuplicateAttempt(Booking booking, Room room, Date date) {
        if (booking != null && booking.getBookingDateList() != null && booking.getBookingDateList() != null) {
            for (int i = 0; i < booking.getRoomList().size() && i < booking.getBookingDateList().size(); i++) {
                if (booking.getRoomList().get(i).getId().equals(room.getId()) && DateUtils.isSameDay(booking.getBookingDateList().get(i), date))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<Date> removeBookingDate(Booking booking, Long roomId) {
        List<Date> newBookingDateList = new ArrayList<>();
        for (int i = 0; i < booking.getRoomList().size(); i++) {
            if (!booking.getRoomList().get(i).getId().equals(roomId))
                newBookingDateList.add(booking.getBookingDateList().get(i));
        }
        return newBookingDateList;
    }

    @Override
    public boolean isBookingInvalid(Booking booking, Room room) {
        // if booking has any room from different hotel
        return booking.getRoomList().stream()
                .filter(r -> !r.getHotel().getId().equals(room.getHotel().getId()))
                .count() > 0;
    }

    @Override
    public List<Booking> findBookings(User currentUser, Integer page, Integer size) {
        List<Booking> bookingList = new ArrayList<>();
        if (currentUser.hasAssignedRole(Constants.Roles.ROLE_USER))
            bookingList = this.findByUser(currentUser, page, size);
        else if (currentUser.hasAssignedRole(Constants.Roles.ROLE_HOTEL_ADMIN)) {
            Hotel hotel = this.hotelService.findByUser(currentUser);
            bookingList = this.findByHotel(hotel, page, size);
        } else if (currentUser.hasAssignedRole(Constants.Roles.ROLE_ADMIN)) {
            bookingList = this.findAll(page, size);
        }
        return bookingList;
    }

    @Override
    public List<Booking> findFiltered(User currentUser, boolean isManual) {
        List<Booking> bookingList = this.findBookings(currentUser, null, null);
        return bookingList
                .stream()
                .filter(booking -> booking.isManualBooking() == isManual)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findFilteredRoomListByPlacementDate(User currentUser, Date date) {
        List<Booking> bookingList = this.findBookings(currentUser, null, null);
        List<Room> roomList = new ArrayList<>();
        for (Booking booking:bookingList){
            if (DateUtils.isSameDay(booking.getCreated(),date))
                roomList.addAll(booking.getRoomList());
        }
        return roomList;
    }

    @Override
    public List<Room> findFilteredRoomListByPlacementDateDistinct(User currentUser,Date date){
        return this.findFilteredRoomListByPlacementDate(currentUser,date).stream()
                .filter(Utils.distinctByKey(BaseEntity::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findFilteredRoomList(User currentUser, Date date) {
        List<Booking> bookingList = this.findBookings(currentUser, null, null);

        List<Room> bookedRooms = new ArrayList<>();
        for (Booking booking : bookingList) {
            List<Room> roomList = booking.getRoomList();
            List<Date> bookingDateList = booking.getBookingDateList();
            for (int i = 0; i < roomList.size() && i < bookingDateList.size(); i++) {
                if (DateUtils.isSameDay(bookingDateList.get(i), date))
                    bookedRooms.add(roomList.get(i));
            }
        }
        return bookedRooms;
    }

}
