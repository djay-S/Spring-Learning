package com.example.async.controller;

import com.example.async.controller.service.RequestService;
import com.example.async.model.Request;
import com.example.async.model.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/async")
public class Controller {

    private final RequestService service;

    @GetMapping(path = "/request/{id}")
    public ResponseEntity<RequestStatus> getRequestStatus(@PathVariable("id") int statusId) {
        return ResponseEntity.ok(service.getRequestStatus(statusId));
    }

    @PostMapping(path = "/request")
    public ResponseEntity<RequestStatus> publishRequest(@RequestBody Request request) {
        return ResponseEntity.ok(service.publishRequest(request));
    }
}
