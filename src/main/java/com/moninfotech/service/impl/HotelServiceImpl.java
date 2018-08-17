package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.commons.pojo.ParamFacilities;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import com.moninfotech.domain.User;
import com.moninfotech.repository.HotelRepository;
import com.moninfotech.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 3/26/17.
 */
@Service
public class HotelServiceImpl implements HotelService {
    @PersistenceContext
    private EntityManager em;

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
    public List<Hotel> searchHotel(String query, int page) {
        List<Hotel> hotelList = this.hotelRepo.findDistinctByNameContainingIgnoreCaseOrAddressAreaContainingIgnoreCaseOrAddressUpazilaContainingIgnoreCase(query, query, query, new PageRequest(page, SortAttributes.Page.SIZE, Sort.Direction.DESC, SortAttributes.FIELD_ID));
        return hotelList;
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

    @Override
    public List<Hotel> filter(String query, String star, String price, int rating, ParamFacilities facilities) {
        String[] prices = price.split("x");
        return this.hotelRepo.filterHotels(
                query,
                star,
                Integer.parseInt(prices[0]),
                Integer.parseInt(prices[1]), rating,
                facilities.getRestaurant(),
                facilities.getLift(),
                facilities.getWifi(),
                facilities.getConferenceRoom(),
                facilities.getSwimingPool(),
                facilities.getSportsZone(),
                facilities.getGym(),
                facilities.getSpa(),
                facilities.getBar(),
                facilities.getHelipad(),
                facilities.getAc(),
                facilities.getFrontDesk(),
                facilities.getRoomService(),
                facilities.getElectricity(),
                facilities.getSecurity(),
                facilities.getComplementaryBreakfast(),
                facilities.getCoffeeShop(),
                facilities.getFreeParking(),
                facilities.getAirportTransportation(),
                facilities.getBusinessCenter(),
                facilities.getMeetingRoom(),
                facilities.getDoctorOnCall(),
                facilities.getWakeupService(),
                facilities.getWheelChairAccessible(),
                facilities.getSafeDepositBox(),
                facilities.getNonSmokingRoom(),
                facilities.getVipFloor(),
                facilities.getAcceptCreditCard(),
                facilities.getNewspaper(),
                facilities.getLaundry(),
                facilities.getLobby(),
                facilities.getForeignExchange(),
                facilities.getExecutiveLaunge(),
                facilities.getBabySitting()
        );
    }


//    public String buildFilterQuery(String query, String star, int rating, Map<String, String> fMap) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT hotel.id as id, room.id as room_id," +
//                " hotel.name" +
//                " FROM hotel LEFT JOIN room ON hotel.id=room.hotel_id WHERE hotel.area LIKE '%").append(query).append("%' AND hotel.star LIKE '%").append(star).append("%'  AND ROUND(hotel.rating)=").append(rating);
//        for (Map.Entry<String, String> entry : fMap.entrySet()) {
//            String key = entry.getKey();
//            key = Utils.toSnakeCase(key);
//            boolean value = Boolean.parseBoolean(entry.getValue());
//            if (value)
//                builder.append(" AND hotel.").append(key).append("=").append(true);
//        }
//        builder.append(" GROUP BY id");
//        return builder.toString();
////        "AND restaurant=:restaurant AND room.price BETWEEN :priceFrom AND :priceTo GROUP BY hotel.id
//    }
}
