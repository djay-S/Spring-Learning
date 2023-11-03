package com.ratham.beassignment.dao;

import com.ratham.beassignment.model.SchoolUser;
import com.ratham.beassignment.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolUserRepository extends JpaRepository<SchoolUser, Integer> {
    Optional<SchoolUser> findSchoolUserById(Integer teacherId);
    Optional<SchoolUser> findSchoolUserByEmail(String email);
}
