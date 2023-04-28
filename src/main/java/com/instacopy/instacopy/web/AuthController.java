package com.instacopy.instacopy.web;

import com.instacopy.instacopy.payload.request.LoginRequest;
import com.instacopy.instacopy.payload.request.SighupRequest;
import com.instacopy.instacopy.payload.response.JWTTokenSuccessResponse;
import com.instacopy.instacopy.payload.response.MessageResponse;
import com.instacopy.instacopy.security.JWTTokenProvider;
import com.instacopy.instacopy.security.SecurityConstans;
import com.instacopy.instacopy.service.UserService;
import com.instacopy.instacopy.validations.ResposeErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private ResposeErrorValidation resposeErrorValidation;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                   BindingResult bindingResult){

        ResponseEntity<Object> errors = resposeErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstans.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true,jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registrateUser(@Valid @RequestBody SighupRequest sighupRequest,
                                                 BindingResult bindingResult){
        ResponseEntity<Object> errors = resposeErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(sighupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

    }
}
