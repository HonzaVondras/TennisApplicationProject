package com.inqoolApp.tennis.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.inqoolApp.tennis.GeneralRepository;

import jakarta.transaction.Transactional;

@Controller
public class UserController {
    
    @Autowired
    private final GeneralRepository<User> userRepository;

    @Autowired
    public UserController(GeneralRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<User> getUserByPhoneNumber(@PathVariable Long phoneNUmber) {
        User userObj = userRepository.findById(phoneNUmber);
        return new ResponseEntity<>(userObj, HttpStatus.OK);
        //if (courtObj.isPresent()) {
        //    return new ResponseEntity<>(courtObj.get(), HttpStatus.OK);
        //} else {
        //    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //}
    }

    @Transactional
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User userObj = userRepository.save(user);
        return new ResponseEntity<>(userObj, HttpStatus.CREATED);

    }
}