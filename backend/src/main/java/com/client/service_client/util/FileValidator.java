package com.client.service_client.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.response.ResponseOnlyMessage;

public class FileValidator{
    public static ResponseEntity<?> validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseOnlyMessage("File is required"));
        }

        String mimeType = file.getContentType();

        if (mimeType == null) {
            return ResponseEntity.unprocessableEntity().body(new ResponseOnlyMessage("Unexpected error"));
        }

        if (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png")) {
            return ResponseEntity.badRequest().body(new ResponseOnlyMessage("Formated file is not correct: " + mimeType));
        }

        return null;
    }
}
