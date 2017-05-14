package com.moninfotech.commons;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
public class FileIO {

    public static File convertToFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
        }
        return file;
    }

    public static List<byte[]> convertMultipartFiles(MultipartFile[] multipartFiles) {
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
}
