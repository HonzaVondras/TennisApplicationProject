package com.inqoolApp.tennis.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.inqoolApp.tennis.GeneralRepository;

import jakarta.transaction.Transactional;

/**
 * Class that handles all work with the Users table in the database
 *
 * @author Jan Vondrasek
*/

@Controller
public class UserController {
    
    @Autowired
    private final GeneralRepository<User> userRepository;


    /**
    * Constructor for UserController.
    *
    * Initializes the UserController with the provided UserRepository instance.
    *
    * @param userRepository offers operations on User entities, such as saving and retrieving users from the database
    */
    @Autowired
    public UserController(GeneralRepository<User> userRepository) {
        this.userRepository = userRepository;
    }


    /**
    * Retrieves all users.
    *
    * This method retrieves all users from the database.
    *
    * @return ResponseEntity containing a list of all users and status OK
    */
    @Transactional
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userObjects = userRepository.findAll();
        return new ResponseEntity<>(userObjects, HttpStatus.OK);
    }

    /**
    * Retrieves a user by ID.
    *
    * This method retrieves a user from the database based on the specified ID.
    *
    * @param id the unique identifier of the user to retrieve
    * @return ResponseEntity containing the user with the specified ID and status OK
    */
    @Transactional
    public ResponseEntity<User> getUserById(Long id) {
        User userObj = userRepository.findById(id);
        return new ResponseEntity<>(userObj, HttpStatus.OK);
    }

    /**
    * Retrieves a user by phone number.
    *
    * This method retrieves a user from the database based on the specified phone number.
    *
    * @param phoneNumber the phone number of the user to retrieve
    * @return ResponseEntity containing the user with the specified phone number and status OK
    */
    @Transactional
    public ResponseEntity<User> getUserByPhoneNumber(Long phoneNUmber) {
        User userObj = userRepository.findById(phoneNUmber);
        return new ResponseEntity<>(userObj, HttpStatus.OK);
    }

    /**
    * Adds a new user.
    *
    * This method adds a new user to the database.
    *
    * @param user the user object to be added
    * @return ResponseEntity containing the added user and status OK if the user is successfully added.
    */
    @Transactional
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User userObj = userRepository.save(user);
        return new ResponseEntity<>(userObj, HttpStatus.OK);

    }
}