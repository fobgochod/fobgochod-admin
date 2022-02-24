package com.fobgochod.exception;

import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.domain.base.StdError;
import com.fobgochod.domain.enumeration.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常捕获
 *
 * @author seven
 * @date 2020/6/7
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@RestControllerAdvice
public class FghExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FghExceptionHandler.class);

    private static StdError printError(HttpServletRequest req, Exception e, I18nCode errorCode) {
        StdError stdError = StdError.of(ErrorType.Unexpected, errorCode.getCode(), e.getMessage(), req.getRequestURI());
        logger.error(stdError.toString(), e);
        return stdError;
    }

    @ExceptionHandler(FghException.class)
    public StdError fgh(HttpServletRequest req, FghException e) {
        StdError stdError = StdError.of(e, req.getRequestURI());
        logger.error(stdError.toString(), e);
        return stdError;
    }

    @ExceptionHandler(Exception.class)
    public StdError unexpected(HttpServletRequest req, Exception e) {
        return printError(req, e, I18nCode.UNEXPECTED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public StdError notFound(HttpServletRequest req, ServletException e) {
        return printError(req, e, I18nCode.NOT_FOUND);
    }

    @ExceptionHandler(DataAccessException.class)
    public StdError dataAccess(HttpServletRequest req, DataAccessException e) {
        return printError(req, e, I18nCode.DATA_ACCESS);
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public StdError validation(HttpServletRequest req, Exception e) {
        StdError stdError = printError(req, e, I18nCode.VALIDATION);
        StringBuilder builder = new StringBuilder();
        if (e instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> error : ((ConstraintViolationException) e).getConstraintViolations()) {
                builder.append(error.getPropertyPath()).append(": ").append(error.getMessage()).append("; ");
            }
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            BindingResult result = methodArgumentNotValidException.getBindingResult();
            final List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError error : fieldErrors) {
                builder.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }
        }
        stdError.setMessage(builder.toString().trim());
        return stdError;
    }
}
