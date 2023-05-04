package com.instacopy.instacopy.service;

import com.instacopy.instacopy.entity.User;
import com.instacopy.instacopy.repository.ImageRepository;
import com.instacopy.instacopy.repository.PostRepository;
import com.instacopy.instacopy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageService {

    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository, PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    private byte[] compressImage(byte[] image){
        Deflater deflater = new Deflater();
        deflater.setInput(image);
        deflater.finish();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(image.length);
        byte[] buffer = new byte[1024];

        try {
            while(!deflater.finished()){
                int count = deflater.deflate(buffer);
                byteArrayOutputStream.write(buffer,0,count);
            }
            byteArrayOutputStream.close();
        }catch (IOException e){
            LOG.error("Can not compress bytes:" + e.getMessage());
        }

        return byteArrayOutputStream.toByteArray();
    }

    private byte[] decompressImage(byte[] image){
        Inflater inflater = new Inflater();
        inflater.setInput(image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(image.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()){
                int count = inflater.inflate(buffer);
                byteArrayOutputStream.write(buffer,0,count);
            }
            byteArrayOutputStream.close();
        }catch (IOException | DataFormatException e){
            LOG.error("Can decompress image:"+ e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }

    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User with username "+username+" not found"));
    }


}
