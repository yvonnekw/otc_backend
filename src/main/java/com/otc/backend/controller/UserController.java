package com.otc.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.Users;
import com.otc.backend.publisher.RabbitMQJsonProducer;
import com.otc.backend.publisher.RabbitMQProducer;
import com.otc.backend.services.TokenService;
import com.otc.backend.services.UserService;

import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {


    private final RabbitMQProducer rabbitMQProducer;
    private final UserService userService;
    private final TokenService tokenService;

    private RabbitMQJsonProducer rabbitMQJsonProducer;

   // @Autowired
    public UserController(UserService userService, TokenService tokenService,
            RabbitMQProducer rabbitMQProducer, 
            RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.rabbitMQProducer = rabbitMQProducer;
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }
    
    @PostMapping("/mess-publisher")
    public ResponseEntity<String> sendJsonMessage(@RequestBody Users user) {
        rabbitMQJsonProducer.sendJsonMessage(user);
        return ResponseEntity.ok("Json message sent RabbitMQ...");
    }

    @GetMapping("/verify")
    public Users verifyIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = "";
        Users user;

        if(token.substring(0,6).equals("Bearer")) {
            String strippedToken = token.substring(7);
            username = tokenService.getUsernameFromToken(strippedToken);
        }
        try {
            user = userService.getUserByUsername(username);
        } catch(Exception e) {
            user = null;
        }
        
        return user;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloUserController(@RequestParam("message") String message) {
        rabbitMQProducer.sendMessage(message);
        // return "User access level";
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }

    @GetMapping("/")
    public ResponseEntity<String> helloUserController2() {
        rabbitMQProducer.sendMessage("User access level new message");
        // return "User access level";
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") Long userId, @RequestBody Users updatedUser){
       Users userDto = userService.updateUser(userId, updatedUser);
       return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCall(@PathVariable("id") Long userId){
       userService.deleteUser(userId);
       return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/users")
    public String users() {
        return "my users";
    }
/* 
    @GetMapping("/{username}/phonenumbers")
    public ResponseEntity<List<String>> getPhoneNumbersForUser(@PathVariable String username) {
    List<String> phoneNumbers = callReceiverService.getPhoneNumbersForUser(username);
    return ResponseEntity.ok(phoneNumbers);
    }*/
    
    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Users>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.FOUND);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserByUserName(@PathVariable("username") String username) {
        try {
            Users theUser = userService.getUserByUsername(username);
            return ResponseEntity.ok(theUser);
        } catch (UserDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }

    
}
