package com.wlkg.common.advice;

import com.wlkg.common.dto.ExceptionResult;
import com.wlkg.common.exception.WlkgException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author 飞鸟
 * @create 2019-12-04 10:15
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> handleException(WlkgException e) {
        ExceptionResult result = new ExceptionResult(e.getExceptionEnums());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
