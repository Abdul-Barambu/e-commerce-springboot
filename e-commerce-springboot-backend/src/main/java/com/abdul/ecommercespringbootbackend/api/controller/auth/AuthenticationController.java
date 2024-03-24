package com.abdul.ecommercespringbootbackend.api.controller.auth;

import com.abdul.ecommercespringbootbackend.api.model.LoginBody;
import com.abdul.ecommercespringbootbackend.api.model.LoginResponse;
import com.abdul.ecommercespringbootbackend.api.model.RegistrationBody;
import com.abdul.ecommercespringbootbackend.exception.UserAlreadyExistException;
import com.abdul.ecommercespringbootbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        }catch (UserAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
        String jwt = userService.loginUser(loginBody);
        if(jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);

            return ResponseEntity.ok(response);
        }
    }

}
