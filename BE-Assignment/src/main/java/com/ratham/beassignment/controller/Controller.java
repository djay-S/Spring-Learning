package com.ratham.beassignment.controller;

import com.ratham.beassignment.model.SchoolUser;
import com.ratham.beassignment.model.Slot;
import com.ratham.beassignment.model.Student;
import com.ratham.beassignment.service.SchoolService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/school")
@AllArgsConstructor
public class Controller {

    private final SchoolService schoolService;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody SchoolUser user) {
        try {
            boolean isLoginUserValid = schoolService.validateUser(user);

            if (!isLoginUserValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String userJwt = schoolService.generateLoggedInUserJwt(user);
            return ResponseEntity.ok("Token: " + userJwt);
        }
        catch (UsernameNotFoundException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(path = "/register")
    public ResponseEntity<String> registerUser(@RequestBody SchoolUser user) {
        try {
            schoolService.registerUser(user);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/slot")
    public ResponseEntity<Slot> registerUserSlot(@RequestBody Slot slot,
                                                 @RequestHeader("Authorization") String token) {
        Slot registeredSlot;
        try {
            boolean isJwtValid = schoolService.validateJwt(token, slot.getBookedBy());

            if (!isJwtValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            registeredSlot = schoolService.registerUserSlot(slot);
            if (registeredSlot != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(registeredSlot);
            }
        }
        catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(path = "/slot/{id}")
    public ResponseEntity<List<Slot>> getUserSlots(@PathVariable("id") int userId,
                                                   @RequestHeader("Authorization") String token) {
        List<Slot> slotsByUserId;

        try {
            boolean isJwtValid = schoolService.validateJwt(token, userId);

            if (!isJwtValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            slotsByUserId = schoolService.getSlotsByUserId(userId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(slotsByUserId);
    }
}
