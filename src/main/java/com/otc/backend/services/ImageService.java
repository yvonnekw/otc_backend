package com.otc.backend.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import com.otc.backend.models.Image;
import com.otc.backend.repository.ImageRepository;

@Service
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    private static final File DIRECTORY = new File(System.getProperty("user.dir")+"javaProject/optical_tele_company/backend/otc/img");
    private static final String URL = "http://localhost:8000/images/";

    @Autowired
    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public String uploadImage(MultipartFile file, String prefix) {
        try {
            //Then contentent type from request img/jpeg
            String extension = "." + file.getContentType().split("/")[1];

            File img = File.createTempFile(prefix, extension, DIRECTORY);
            file.transferTo(img);
            
            String imageURL = URL + img.getName();
            Image i = new Image(img.getName(), file.getContentType(), img.getPath(), imageURL);
            
            Image savedImage = imageRepository.save(i);

            return "file upload successfully: " + img.getName();
       
        } catch (IOException e) {
            e.printStackTrace();
            return  "file upload not successful: ";
        }

       
    }

    
}
