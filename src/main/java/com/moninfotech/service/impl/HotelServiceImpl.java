package com.moninfotech.service.impl;

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
    @Autowired
    private HotelRepository hotelRepo;

    // returns all hotels paginated
    @Override
    public List<Hotel> findAll(int page, int size, String sortBy, boolean isDesc) {
        if (sortBy == null || sortBy.isEmpty()) sortBy = Constants.FIELD_ID;
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
    public List<Hotel> findByAddressArea(String area) {
        return this.hotelRepo.findByAddressAreaIgnoreCase(area);
    }

    @Override
    public List<Hotel> findByAddressUpazila(String location) {
        return this.hotelRepo.findByAddressUpazilaIgnoreCase(location);
    }

    @Override
    public List<Hotel> filterUnbookedHotelsByDate(List<Hotel> hotels, Date startDate, Date endDate) {
        List<Hotel> hotelList = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.hasUnbookedRoom(startDate, endDate))
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
            if (filterType.equals(FilterType.STAR))
                return hotelList.stream()
                        .filter(x -> x.getStar() == Byte.parseByte(value))
                        .collect(Collectors.toList());
            else if (filterType.equals(FilterType.PRICE)) {
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
            }
        } catch (NumberFormatException e) {
            System.out.println("filterHotels(List<Hotel> hotelList, String filterType, String value): " + e.toString());
        }
        return new ArrayList<>();
    }

}
