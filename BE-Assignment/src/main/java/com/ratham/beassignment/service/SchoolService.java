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

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

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
        List<Slot> slots = slotRepository.findSlotsByStudentIdOrTeacherId(userId, userId).orElse(new ArrayList<>());
        if (slots.isEmpty())
            return slots;
        return slots.stream().filter(slot -> slot.getSlotStartTs().after(new Date())).toList();
    }

    public Slot registerUserSlot(Slot slot) {
        if (slot == null)
//            return null;
            throw new IllegalStateException("Blank Slot Entered");
        Optional<Slot> slotsBySlotStartTsOptional = slotRepository.findSlotsBySlotStartTs(slot.getSlotStartTs());
        if (slotsBySlotStartTsOptional.isPresent()) {
            Slot existingSlot = slotsBySlotStartTsOptional.get();
            if (existingSlot.getBookedBy() != slot.getBookedBy()) {
                throw new IllegalStateException("Slot is already booked, select another slot");
            }
            else {
                throw new IllegalStateException("Slot is Already booked");
            }
        }
        slot.setBookedTs(new Date());
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

    public List<Slot> getAvailableSlotsForMonth() {
        List<Slot> slots = createBlankSlotsForTheMonth();
        Optional<List<Slot>> savedSlotsOptional = slotRepository.findSlotsBySlotStartTsAfter(new Date());
        if (savedSlotsOptional.isPresent()) {
            List<Slot> savedSlots = savedSlotsOptional.get();
            List<Date> savedSlotsDates = savedSlots.stream().map(Slot::getSlotStartTs).toList();
            slots = slots.stream().filter(slot -> !savedSlotsDates.contains(slot.getSlotStartTs())).toList();
        }
        logger.info("{} free slots found as of: {}", slots.size(), new Date());
        return slots;
    }

    private List<Slot> createBlankSlotsForTheMonth() {
        List<Slot> slots = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(10, 0))) {
            if (today.getDayOfWeek() == DayOfWeek.THURSDAY) {
                today = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            }
            else {
                today = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
            }
        }

        for (LocalDate date = today; date.getMonthValue() == today.getMonthValue(); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.THURSDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                Slot slot = new Slot();
                LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(10, 0));
                Date slotDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                slot.setSlotStartTs(slotDate);

                slots.add(slot);
            }
        }
        return slots;
    }
}
