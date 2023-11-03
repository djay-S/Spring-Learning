package com.ratham.beassignment.dao;

import com.ratham.beassignment.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Optional<Staff> findStaffById(Integer teacherId);
    Optional<Staff> findStaffByEmail(String email);

}
