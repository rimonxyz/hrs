package com.moninfotech.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moninfotech.commons.Constants;
import com.moninfotech.commons.utils.DateUtils;
import com.moninfotech.config.security.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sayemkcn on 3/21/17.
 */
@Entity
public class Room extends BaseEntity {
    private String roomNumber;
    private int price;
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(length = 1000000)
    @JsonIgnore
    private List<byte[]> images;
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "date")
    private Map<Date, Integer> discountMap;
    private int discount;
    private int agentDiscount;
    private boolean discounted;
    private int floorNumber;
    private boolean archived;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "roomList")
    @JsonBackReference
    private List<Booking> bookingList;
    @ManyToOne
    @JsonBackReference
    private Hotel hotel;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Category category;

    @Embedded
    private Facilities facilities;

    @PrePersist
    @PreUpdate
    public void setDiscounted() {
        if (this.discount > 0)
            this.setDiscounted(true);
        else this.setDiscounted(false);
    }

    public int getDiscountedPrice(Date date) {
        int x = this.price - this.getDiscount(date);
        if (x < 0)
            return 0;
        return x;
    }

    public int getDiscountedPrice(String dateStr) {
        Date date = parseDate(dateStr);
        return getDiscountedPrice(date);
    }

    //    public int getDiscount() {
//        if (this.discountMap != null)
//            for (Map.Entry<Date, Integer> entry : discountMap.entrySet()) {
//                // todays discount from map
//                if (DateUtils.isSameDay(entry.getKey(), new Date()))
//                    // check if discount exceeds the original price
//                    if (entry.getValue() < this.price)
//                        return entry.getValue();
//            }
//        return discount;
//    }
    public int getDiscount(Date date) {
        int discount = 0;
        if (this.discountMap != null)
            for (Map.Entry<Date, Integer> entry : discountMap.entrySet()) {
                // todays discount from map
                if (DateUtils.isSameDay(entry.getKey(), date))
                    // check if discount exceeds the original price
                    if (entry.getValue() < this.price) {
                        discount = entry.getValue();
                        break;
                    }
            }

        // check if discount is still zero. assign default discount amount if true
        if (discount <= 0) discount = this.discount;

        if (!SecurityConfig.isAuthenticated()) return discount;

        // for agent add their special discount with original discount;
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser != null && loggedInUser.hasAssignedRole(Constants.Roles.ROLE_AGENT))
            discount = discount + this.agentDiscount;
        return discount;
    }

    public int getDiscount() {
        return discount;
    }

    public int getDiscount(String dateStr) {
        Date date = parseDate(dateStr);
        return getDiscount(date);
    }

    @SuppressWarnings("UsedOnThymeleaf")
    public String getDiscountPercentage(Date date) {
        return String.valueOf((this.getDiscount(date) * 100) / price) + "%";
    }

    @SuppressWarnings("UsedOnThymeleaf")
    public String getDiscountPercentage(String dateStr) {
        Date date = parseDate(dateStr);
        return getDiscountPercentage(date);
    }

    private Date parseDate(String dateStr) {
        Date date = new Date();
        try {
            if (dateStr != null)
                date = DateUtils.getParsableDateFormat().parse(dateStr);
        } catch (ParseException ignored) {

        }
        return date;
    }

//    // check if this room is booked for a given date of current month
//    public boolean isBooked(int day) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.set(Calendar.DATE, day);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        for (Booking booking : this.bookingList) {
//            if (DateUtils.isInBetween(booking.getStartDate(), booking.getEndDate(), cal.getTime()))
//                return true;
//        }
//        return false;
//    }


    // gives booking status for a specific date for a room
    public boolean isBooked(Date date) {
        for (Booking booking : this.bookingList) {
            // check if booking is confirmed
            if (booking.isConfirmed() && !booking.isCancelled())
                if (DateUtils.containsDate(booking.getBookingDateList(), date))
                    return true;
        }
        return false;
    }

    // returns booking dates(String) for that room placed on today
    public Map<String, Object> getBookingDatesForView(String placementDateStr) throws ParseException {
        Collection<Date> bookingDates = this.getBookingDates(DateUtils.getParsableDateFormat().parse(placementDateStr));
        String bookingDatesStr = bookingDates.stream()
                .map(date -> DateUtils.getReadableDateFormat().format(date))
                .collect(Collectors.joining(" | "));
        Map<String,Object> map = new HashMap<>();
        map.put("dates",bookingDatesStr);
        map.put("size",bookingDates.size());
        return map;
    }

    // returns booking dates for that room placed on today
    public Collection<Date> getBookingDates(Date placementDate) {
        Set<Date> dateList = new HashSet<>();
        for (Booking booking : this.bookingList) {
            // check if booking is confirmed
            if (booking.isConfirmed() && !booking.isCancelled()) {
                // filter bookings of this room by todays date
                if (DateUtils.isSameDay(booking.getCreated(), placementDate)) {
                    dateList.addAll(this.getBookingDates(this, booking));
                }
            }
        }
        return dateList;
    }

    // all booking dates for a room in a booking
    private List<Date> getBookingDates(Room room, Booking booking) {
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < booking.getRoomList().size() && i < booking.getBookingDateList().size(); i++) {
            // if this rooms are same
            if (room.getId().equals(booking.getRoomList().get(i).getId())) {
                dateList.add(booking.getBookingDateList().get(i));
            }
        }
        return dateList;
    }

    public List<Integer> getDaysCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int myMonth = cal.get(Calendar.MONTH);

        List<Integer> dateList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        while (myMonth == cal.get(Calendar.MONTH)) {
//            System.out.print(cal.getTime());
            dateList.add(Integer.parseInt(simpleDateFormat.format(cal.getTime())));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }

//    public String getBookingDatesAsString() {
//        // get all booked dates of this room
//        List<LocalDate> dates = new ArrayList<>();
//        if (this.bookingList != null) {
//            for (Booking booking : this.bookingList) {
//                dates.addAll(DateUtils.getDates(booking.getStartDate(), booking.getEndDate()));
//            }
//        }
//        // convert all dates into a string
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < dates.size(); i++) {
//            LocalDate localDate = dates.get(i);
//            if (localDate.isBefore(LocalDate.now()))
//                continue;
//            DateTimeFormatter formatter;
//            if (localDate.getMonth().equals(LocalDate.now().getMonth()))
//                formatter = DateTimeFormatter.ofPattern("dd");
//            else
//                formatter = DateTimeFormatter.ofPattern("dd MMM");
//            if (i == dates.size() - 1)
//                stringBuilder.append(localDate.format(formatter));
//            else
//                stringBuilder.append(localDate.format(formatter) + ",\n");
//        }
//        return stringBuilder.toString();
//    }


    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }


    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public Map<Date, Integer> getDiscountMap() {
        return discountMap;
    }

    public Map<Date, Integer> getDiscountMapSorted() {
        return new TreeMap<>(discountMap);
    }

    public void setDiscountMap(Map<Date, Integer> discountMap) {
        this.discountMap = discountMap;
    }

    public Facilities getFacilities() {
        return facilities;
    }

    public void setFacilities(Facilities facilities) {
        this.facilities = facilities;
    }

    public int getAgentDiscount() {
        return agentDiscount;
    }

    public void setAgentDiscount(int agentDiscount) {
        this.agentDiscount = agentDiscount;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
