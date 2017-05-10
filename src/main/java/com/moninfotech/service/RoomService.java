package com.moninfotech.service;

import com.moninfotech.domain.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
public interface RoomService {
    Room save(Room room);

    List<byte[]> convertMultipartFiles(MultipartFile[] multipartFiles);

    Room findOne(Long id);

    List<Room> findAll(int page, int size);

    void delete(Long id);

    List<Room> findAll(Long[] ids);

    List<Long> filterRoomIds(List<Room> roomList, String filterType, String value);
}
