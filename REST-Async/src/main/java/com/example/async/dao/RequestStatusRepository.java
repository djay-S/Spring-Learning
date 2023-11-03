package com.example.async.dao;

import com.example.async.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {
    Optional<RequestStatus> findRequestStatusByStatusId(int statusId);
}
