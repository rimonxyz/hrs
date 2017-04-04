package com.moninfotech.service.impl;

import com.moninfotech.commons.FileIO;
import com.moninfotech.domain.Room;
import com.moninfotech.repository.RoomRepository;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
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
                System.out.println(e.toString());
            }
        }

        return filesList;
    }
}
