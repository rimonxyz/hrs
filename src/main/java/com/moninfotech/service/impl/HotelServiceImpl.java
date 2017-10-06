package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.repository.HotelRepository;
import com.moninfotech.service.HotelService;
import com.moninfotech.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepo;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    @Override
    public List<Hotel> findAll() {
        return this.hotelRepo.findAll();
    }

    // returns all hotels paginated
    @Override
    public List<Hotel> findAll(int page, int size, String sortBy, boolean isDesc) {
        if (sortBy == null || sortBy.isEmpty()) sortBy = SortAttributes.FIELD_ID;
        if (isDesc)
            return this.hotelRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, sortBy)).getContent();
        return this.hotelRepo.findAll(new PageRequest(page, size, Sort.Direction.ASC, sortBy)).getContent();
    }

    // save a hotel
    @Override
    public Hotel save(Hotel hotel) {
        return this.hotelRepo.save(hotel);
    }

    // find a hotel by id
    @Override
    public Hotel findOne(Long id) {
        return this.hotelRepo.findOne(id);
    }

    @Override
    public void delete(Long id) {
        this.hotelRepo.delete(id);
    }

    @Override
    public Hotel findByUser(User user) {
        return this.hotelRepo.findByUser(user);
    }

    @Override
    public List<Hotel> findByAddressArea(String area,int page) {
        return this.hotelRepo.findByAddressAreaContainingIgnoreCase(area,new PageRequest(page,SortAttributes.Page.SIZE,Sort.Direction.ASC,SortAttributes.FIELD_ID));
    }

    @Override
    public List<Hotel> findByAddressUpazila(String location,int page) {
        return this.hotelRepo.findByAddressUpazilaContainingIgnoreCase(location,new PageRequest(page,SortAttributes.Page.SIZE,Sort.Direction.ASC,SortAttributes.FIELD_ID));
    }

    @Override
    public List<Hotel> filterUnbookedHotelsByDate(List<Hotel> hotels, Date date) {
        List<Hotel> hotelList = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.hasUnbookedRoom(date))
                hotelList.add(hotel);
        }
        return hotelList;
    }

    @Override
    public List<String> getAddressAreaAndUpazilaList() {
        List<String> areaList = new ArrayList<>();
        this.hotelRepo.findAll().forEach(hotel -> {
            if (hotel.getAddress().getArea() != null && !areaList.contains(hotel.getAddress().getArea()))
                areaList.add(hotel.getAddress().getArea());
            if (hotel.getAddress().getUpazila() != null && !areaList.contains(hotel.getAddress().getUpazila()))
                areaList.add(hotel.getAddress().getUpazila());
        });
        return areaList;
    }

    @Override
    public List<Hotel> filterHotels(List<Hotel> hotelList, String filterType, String value) {
        try {
            switch (filterType) {
                case FilterType.HOTEL_NAME:
                    return hotelList.stream()
                            .filter(hotel -> hotel.getName().toLowerCase().contains(value.toLowerCase()))
                            .collect(Collectors.toList());
                case FilterType.STAR:
                    return hotelList.stream()
                            .filter(x -> x.getStar().equals(value))
                            .collect(Collectors.toList());
                case FilterType.PRICE:
                    String[] range = value.split("x");
                    List<Hotel> newList = new ArrayList<>();
                    for (Hotel hotel : hotelList) {
                        for (Room room : hotel.getRoomList()) {
                            if (room.getPrice() >= Integer.parseInt(range[0]) && room.getPrice() <= Integer.parseInt(range[1])) {
                                newList.add(hotel);
                                break;
                            }
                        }
                    }
                    return newList;
                case FilterType.RATING:
                    return hotelList.stream()
                            .filter(hotel -> Math.round(hotel.getAverageRating()) == Math.round(Float.parseFloat(value)))
                            .collect(Collectors.toList());
                case FilterType.AC:
                    boolean ac = value.equals("ac");
                    return hotelList.stream()
                            .filter(hotel -> hotel.getType().equals(Hotel.Type.SHIP) && hotel.getFacilities().isAc()==ac)
                            .collect(Collectors.toList());
            }
        } catch (NumberFormatException e) {
            System.out.println("filterHotels(List<Hotel> hotelList, String filterType, String value): " + e.toString());
        }
        return hotelList;
    }

    @Override
    public List<Hotel> filterHotels(List<Hotel> hotelList, String type) {
        if (type.equals(Hotel.Type.BOTH)) return hotelList;
        return hotelList.stream()
                .filter(hotel -> hotel.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public List<Hotel> findByNameContaining(String name,int page) {
        return this.hotelRepo.findByNameContainingIgnoreCase(name,new PageRequest(page,SortAttributes.Page.SIZE,Sort.Direction.ASC,SortAttributes.FIELD_ID));
    }

}
