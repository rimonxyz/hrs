package com.moninfotech.commons.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public static boolean isNotEmpty(MultipartFile[] multipartFiles) {
        if (multipartFiles == null) return false;
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty())
                return true;
        }
        return false;
    }

    public static byte[] getScaledImage(byte[] image, int width, int height) throws IOException {
//        InputStream is = new ByteArrayInputStream(image);
//        OutputStream os = new ByteArrayOutputStream();
//        Thumbnails.of(is)
//                .size(width, height)
//                .toOutputStream(os);
//        os.write(image);
        Image img = ImageIO.read(new ByteArrayInputStream(image));
        BufferedImage bi = createResizedCopy(img,width,height,true);

        return getBytes(bi);
    }

    private static BufferedImage createResizedCopy(Image originalImage,
                                                   int scaledWidth, int scaledHeight,
                                                   boolean preserveAlpha) {
        System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public static byte[] getBytes(BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        return baos.toByteArray();
    }

}
