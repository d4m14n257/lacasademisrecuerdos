package com.client.service_client.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class ImageResize {
    public static byte[] resizeImageToBytes(String imagePath, double scale) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File file = new File(imagePath);

        Thumbnails.of(file)
                  .scale(scale)
                  .toOutputStream(baos);

        return baos.toByteArray();
    }
}
