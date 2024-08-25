package com.minsproject.matchpoint.exception;

import com.minsproject.matchpoint.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MatchPointException.class)
    public ResponseEntity<Response<Void>> applicationHandler(MatchPointException e) {
        log.error("Exception occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getMessage()));
    }
}
