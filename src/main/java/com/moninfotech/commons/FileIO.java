package com.moninfotech.commons;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by sayemkcn on 4/4/17.
 */
public class FileIO {

    public static File convertToFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
