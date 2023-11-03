package com.ratham.beassignment.service;

import com.ratham.beassignment.dao.SchoolUserRepository;
import com.ratham.beassignment.dao.SlotRepository;
import com.ratham.beassignment.dao.StaffRepository;
import com.ratham.beassignment.dao.StudentRepository;
import com.ratham.beassignment.model.SchoolUser;
import com.ratham.beassignment.model.Slot;
import com.ratham.beassignment.security.config.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolUserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final SlotRepository slotRepository;
    private final JwtService jwtService;

    private final Logger logger = LoggerFactory.getLogger(SchoolService.class);

    public boolean validateUser(SchoolUser loginUser) {
        boolean isEmailValid = validateEmail(loginUser.getEmail());

        if (!isEmailValid) {
            return false;
        }

        Optional<SchoolUser> loginUserByEmailOptional = userRepository.findSchoolUserByEmail(loginUser.getEmail());

        if (loginUserByEmailOptional.isEmpty()) {
            logger.info("Username not found for email: {}", loginUser.getEmail());
            throw new UsernameNotFoundException("Entered User is not registered");
        }

        if (loginUser.getPassword().equals(loginUserByEmailOptional.get().getPassword()))
            return true;

        throw new IllegalStateException("User Email/Password does not match");
    }

    private boolean validateEmail(String email) {
        return true;
    }

    public void registerUser(SchoolUser user) {
        if (user == null) {
            throw new UsernameNotFoundException("No Details entered");
        }
        userRepository.save(user);
    }

    public List<Slot> getSlotsByUserId(int userId) {
        return slotRepository.findSlotsByStudentIdOrTeacherId(userId, userId).orElse(new ArrayList<>());
    }

    public Slot registerUserSlot(Slot slot) {
        if (slot == null)
//            return null;
            throw new IllegalStateException("Blank Slot Entered");
        return slotRepository.save(slot);
    }

    public String generateLoggedInUserJwt(SchoolUser loggedInUser) {
        return jwtService.generateJwtToken(String.valueOf(loggedInUser.getId()));
    }

    public boolean validateJwt(String token, int userId) {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Bearer Token not shared");
        }

        try {
            return jwtService.validateJwtToken(token, userId);
        }
        catch (JwtException e) {
            logger.error("JwtException occurred: {}", e.getMessage());
            return false;
        }
    }
}
