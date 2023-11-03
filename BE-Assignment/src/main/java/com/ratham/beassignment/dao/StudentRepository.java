package com.ratham.beassignment.dao;

import com.ratham.beassignment.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findStudentById(Integer studentId);
    Optional<Student> findStudentByEmail(String email);

}
