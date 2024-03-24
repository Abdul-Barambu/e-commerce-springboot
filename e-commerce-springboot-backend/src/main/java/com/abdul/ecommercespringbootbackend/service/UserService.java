package com.abdul.ecommercespringbootbackend.service;

import com.abdul.ecommercespringbootbackend.api.model.LoginBody;
import com.abdul.ecommercespringbootbackend.api.model.RegistrationBody;
import com.abdul.ecommercespringbootbackend.exception.UserAlreadyExistException;
import com.abdul.ecommercespringbootbackend.model.LocalUser;
import com.abdul.ecommercespringbootbackend.model.dao.LocalUserDao;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDao localUserDao;
    private EncryptionService encryptionService;
    private JwtService jwtService;

    public UserService(LocalUserDao localUserDao, EncryptionService encryptionService, JwtService jwtService) {
        this.localUserDao = localUserDao;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody)throws UserAlreadyExistException {

        if(localUserDao.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
                || localUserDao.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()){
            throw new UserAlreadyExistException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        return localUserDao.save(user);
    }

    public String loginUser(LoginBody loginBody){
        Optional<LocalUser> optionalLocalUser = localUserDao.findByUsernameIgnoreCase(loginBody.getUsername());
        if(optionalLocalUser.isPresent()){
            LocalUser localUser = optionalLocalUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), localUser.getPassword())){
                return jwtService.generateJwt(localUser);
            }
        }

        return null;
    }
}
