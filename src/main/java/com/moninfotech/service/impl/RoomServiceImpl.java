package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.DateUtils;
import com.moninfotech.commons.FileIO;
import com.moninfotech.commons.pojo.FilterType;
import com.moninfotech.domain.Room;
import com.moninfotech.repository.RoomRepository;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepo;

    @Override
    public Room save(Room room) {
        return this.roomRepo.save(room);
    }

    @Override
    public List<byte[]> convertMultipartFiles(MultipartFile[] multipartFiles) {
        List<byte[]> filesList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                Image image = ImageIO.read(FileIO.convertToFile(multipartFile));
                if (image != null) filesList.add(multipartFile.getBytes());
            } catch (IOException e) {
            }
        }

        return filesList;
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
            return filterRoomIdsByDate(roomList,value);
        else if (filterType.equals(FilterType.CATEGORY))
            return filterRoomIdsByCategory(roomList,value);
        return null;
    }

    private List<Long> filterRoomIdsByCategory(List<Room> roomList, String value) {
        List<Long> idsList = new ArrayList<>();
        roomList.forEach(room -> {
            if (room.getCategory().getName().equals(value))
                idsList.add(room.getId());
        });
        return idsList;
    }

    public List<Long> filterRoomIdsByDate(List<Room> roomList,String value){
        SimpleDateFormat sdf = DateUtils.getParsableDateFormat();
        List<Long> idList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        roomList.forEach(room -> {
            try {
                if (room.isBooked(sdf.parse(value),sdf.parse(value))){
                    idList.add(room.getId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return idList;
    }

}
