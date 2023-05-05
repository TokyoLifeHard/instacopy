package com.instacopy.instacopy.service;

import com.instacopy.instacopy.entity.ImageModel;
import com.instacopy.instacopy.entity.Post;
import com.instacopy.instacopy.entity.User;
import com.instacopy.instacopy.exeptions.ImageNotFoundException;
import com.instacopy.instacopy.repository.ImageRepository;
import com.instacopy.instacopy.repository.PostRepository;
import com.instacopy.instacopy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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

    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);

        if(!ObjectUtils.isEmpty(userProfileImage)){
            imageRepository.delete(userProfileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressImage(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());

        return imageRepository.save(imageModel);
    }

    public ImageModel uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p->p.getId().equals(postId))
                .collect(toSinglePostCollector());

        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(post.getId());
        imageModel.setImageBytes(compressImage(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());

        return imageRepository.save(imageModel);
    }

    public ImageModel getImageToUser(Principal principal){
        User user = getUserByPrincipal(principal);

        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);

        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressImage(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    public ImageModel getImageToPost(Long postId){
        ImageModel imageModel = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Can not find Imaga to Post :"+postId));

        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressImage(imageModel.getImageBytes()));
        }

        return imageModel;
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
    private <T> Collector<T,?,T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list->{
                    if (list.size()!=1){
                        throw new IllegalArgumentException();
                    }
                    return list.get(0);
                }
        );
    }

}
