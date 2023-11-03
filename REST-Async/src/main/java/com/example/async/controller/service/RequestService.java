package com.example.async.controller.service;

import com.example.async.dao.RequestRepository;
import com.example.async.dao.RequestStatusRepository;
import com.example.async.model.Request;
import com.example.async.model.RequestStatus;
import com.example.async.model.Status;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestStatusRepository requestStatusRepository;
    Logger logger = LoggerFactory.getLogger(RequestService.class);

    public RequestStatus getRequestStatus(int statusId) {
        Optional<RequestStatus> requestStatusByStatusId = requestStatusRepository.findRequestStatusByStatusId(statusId);

        return requestStatusByStatusId.orElse(null);
    }

    public RequestStatus publishRequest(Request request) {
        logger.info("Starting publishRequest from thread: {}", Thread.currentThread().getName());
        RequestStatus requestStatus = new RequestStatus(request.getRequestId(), request.getName(), Status.RECEIVED, new Date());
        requestStatusRepository.save(requestStatus);
        try {
            CompletableFuture<Request> requestCompletableFuture = processRequest(request, requestStatus);
            CompletableFuture.allOf(requestCompletableFuture).join();
        }
        catch (Exception e) {
            requestStatus.setStatus(Status.FAILED);
            requestStatusRepository.save(requestStatus);
            logger.info("Exiting publishRequest from thread: {}", Thread.currentThread().getName());
            return requestStatus;
        }
        requestStatus.setStatus(Status.PASSED);
        requestStatusRepository.save(requestStatus);
        return requestStatus;
    }
    
    @Async("asyncTaskExecutor")
    public CompletableFuture<Request> processRequest(Request request, RequestStatus requestStatus) throws Exception {
        logger.info("Starting processRequest from thread: {}", Thread.currentThread().getName());
        Thread.sleep(2000);
        requestStatus.setStatus(Status.PROCESSING);
        requestStatusRepository.save(requestStatus);
        CompletableFuture<Request> saveRequestCompletableFuture = saveRequest(request);
        CompletableFuture.allOf(saveRequestCompletableFuture).join();
        logger.info("Exiting processRequest from thread: {}", Thread.currentThread().getName());
        return CompletableFuture.completedFuture(request);
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<Request> saveRequest(Request request) throws Exception {
        logger.info("Starting saveRequest from thread: {}", Thread.currentThread().getName());
        Request savedRequest = null;
        Thread.sleep(10000);
        if (request.getRequestId() % 7 == 0) {
            throw new IllegalStateException();
        }
        try {
            savedRequest = requestRepository.save(request);
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        logger.info("Exiting saveRequest from thread: {}", Thread.currentThread().getName());
        return CompletableFuture.completedFuture(savedRequest);
    }
}
