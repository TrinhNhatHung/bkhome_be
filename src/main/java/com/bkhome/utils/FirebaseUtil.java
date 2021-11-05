package com.bkhome.utils;

import com.google.cloud.storage.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class FirebaseUtil {

    @Autowired
    private Bucket bucket;

    public String getFileUrl(String fileName) {
        try {
            return bucket.get(fileName).signUrl(100, TimeUnit.DAYS).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> uploadFile(MultipartFile... files) throws IOException {

        List<String> result = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (file == null) {
                    throw new NullPointerException();
                }
                String fileName = file.getOriginalFilename();
                fileName = "" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
                bucket.create(fileName, file.getInputStream(), file.getContentType());
                result.add(fileName);
            }
        }
        return result;
    }
}
