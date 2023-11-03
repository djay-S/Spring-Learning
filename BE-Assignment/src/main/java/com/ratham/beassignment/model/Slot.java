package com.ratham.beassignment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Slot {
    @Id
    @GeneratedValue
    private int slotId;
    private int studentId;
    private int teacherId;
    private int bookedBy;
    private Date bookingStartTs;
    private String token;
}
