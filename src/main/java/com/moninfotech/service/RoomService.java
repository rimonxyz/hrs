package com.moninfotech.service;

import com.moninfotech.domain.Category;
import com.moninfotech.domain.Hotel;
import com.moninfotech.domain.Room;

import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
public interface RoomService {
    Room save(Room room);

    Room findOne(Long id);

    List<Room> findAll(int page, int size);

    void delete(Long id);

    List<Room> findAll(Long[] ids);

    List<Long> filterRoomIds(List<Room> roomList, String filterType, String value);

    List<Room> searchRooms(Hotel hotel, String query);

    List<Room> organiseRoomListCategory(List<Room> roomList, Category newCategory);

    List<Room> saveAll(List<Room> processedRoomList);

    List<Room> removeRoom(List<Room> roomList, Long roomId);
}
