package com.client.service_client.service;

import com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi;
import org.springframework.mock.web.MockMultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    @SuppressWarnings("null")
    public MultipartFile convertImageToWebp(MultipartFile file) throws IOException {
        IIORegistry.getDefaultInstance().registerServiceProvider(new WebPImageReaderSpi());

        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        System.out.println(file.getInputStream());
    
        if (bufferedImage == null) {
            throw new IOException("Error reading image file");
        }
    
        ByteArrayOutputStream webpOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "webp", webpOutputStream);
    
        byte[] webpBytes = webpOutputStream.toByteArray();

        String newFilename = file.getOriginalFilename().replaceAll("\\.[^.]+$", "") + ".webp";
    
        MultipartFile webpMultipartFile = new MockMultipartFile(
            newFilename,
            newFilename,
            "image/webp",
            webpBytes
        );
    
        return webpMultipartFile;
    }
}
