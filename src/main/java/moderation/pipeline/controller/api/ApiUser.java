package moderation.pipeline.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import moderation.pipeline.controller.input.UserDto;
import moderation.pipeline.service.facade.Facade;

@RestController
@RequestMapping("/user")
public class ApiUser {

    @Autowired
    private Facade facade;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto user) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(facade.registerUser(user.getUsername(), user.getPassword()));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers() {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(facade.getAllUsers());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }
}
