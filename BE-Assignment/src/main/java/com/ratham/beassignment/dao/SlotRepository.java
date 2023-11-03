package com.ratham.beassignment.dao;

import com.ratham.beassignment.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {

    Optional<Slot> findSlotBySlotId(Integer slotId);
    Optional<List<Slot>> findSlotsByStudentIdOrTeacherId(Integer studentId, Integer teacherId);
    Optional<List<Slot>> findSlotsBySlotStartTsAfter(Date date);
    Optional<List<Slot>> findSlotsByStudentId(Integer studentId);
    Optional<List<Slot>> findSlotsByTeacherId(Integer studentId);

}
