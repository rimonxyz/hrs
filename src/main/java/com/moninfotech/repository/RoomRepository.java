package com.moninfotech.repository;

import com.moninfotech.domain.Category;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelAndRoomNumberContainingIgnoreCase(Hotel hotel, String query);
    List<Room> findByHotelAndCategory(Hotel hotel, Category category);
}
