package com.example.async.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RequestStatus {
    @Id
    private int statusId;
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date requestTimeStamp;

}
