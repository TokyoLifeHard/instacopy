package com.instacopy.instacopy.service;

import com.instacopy.instacopy.entity.Role;
import com.instacopy.instacopy.entity.User;
import com.instacopy.instacopy.exeptions.UserExistExeption;
import com.instacopy.instacopy.payload.request.SignupRequest;
import com.instacopy.instacopy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public User createUser(SignupRequest sighupRequest){
        User user = new User();
        user.setEmail(sighupRequest.getEmail());
        user.setName(sighupRequest.getFirstname());
        user.setLastname(sighupRequest.getLastname());
        user.setUsername(sighupRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(sighupRequest.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        try {
            LOG.info("Save user {}",sighupRequest.getEmail());
            return userRepository.save(user);
        } catch (Exception e){
            LOG.error("Error during registration {}",e.getMessage());
            throw new UserExistExeption("User "+user.getUsername()+" is already exist");
        }

    }
}
