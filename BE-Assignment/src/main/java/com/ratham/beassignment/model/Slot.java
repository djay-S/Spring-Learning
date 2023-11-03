package com.ratham.beassignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ss Z a")
    private Date slotStartTs;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ss Z a")
    private Date bookedTs;
}
