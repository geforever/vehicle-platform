package org.platform.vehicle.exception;

import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BaseResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {RestController.class})
public class ExceptionHandlerAdvise {


    @ExceptionHandler(BaseException.class)
    @Order(1000)
    public ResponseEntity<BaseResponse> handleWandaException(BaseException be) {
        return new ResponseEntity<>(BaseResponse.failure(be.getCode(), be.getMessage()), HttpStatus.OK);
    }

}

