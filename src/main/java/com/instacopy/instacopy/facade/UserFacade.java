package com.instacopy.instacopy.facade;

import com.instacopy.instacopy.dto.UserDTO;
import com.instacopy.instacopy.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDTO userToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setBiography(user.getBiography());
        return userDTO;
    }
}
