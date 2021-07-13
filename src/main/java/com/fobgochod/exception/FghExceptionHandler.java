package com.fobgochod.exception;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.CommonErrorCode;
import com.fobgochod.domain.enumeration.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.application.name}")
    private String sourceId;
    @Value("${spring.profiles.active:}")
    private String active;

    @ExceptionHandler(Exception.class)
    public StdError unexpected(HttpServletRequest req, Exception e) {
        logger.error(FghLog.getLog(Handler.UNEXPECTED.getName(), e.getMessage(), req), e);

        StdError stdError = StdError.of(sourceId.toUpperCase(), ErrorType.Unexpected.name(), CommonErrorCode.UNEXPECTED, req.getRequestURI());
        stdError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        stdError.setMessage(e.getMessage());
        if (FghConstants.ENV_DEV.equals(active)) {
            stdError.setDebugInfo(StdError.getDebugInfo(req, e));
        }
        return stdError;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class
    })
    public StdError notFound(HttpServletRequest req, ServletException e) {
        logger.error(FghLog.getLog(Handler.NOT_FOUND.getName(), e.getMessage(), req));

        StdError stdError = StdError.of(sourceId.toUpperCase(), ErrorType.System.name(), CommonErrorCode.NOT_FOUND, req.getRequestURI());
        stdError.setCode(HttpStatus.NOT_FOUND.value());
        stdError.setMessage(e.getMessage());
        return stdError;
    }

    @ExceptionHandler(DataAccessException.class)
    public StdError dataAccess(HttpServletRequest req, DataAccessException e) {
        logger.error(FghLog.getLog(Handler.DATA_ACCESS.getName(), e.getMessage(), req), e);

        StdError stdError = StdError.of(sourceId.toUpperCase(), ErrorType.System.name(), CommonErrorCode.DATA_ACCESS, req.getRequestURI());
        stdError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        stdError.setMessage(e.getMessage());
        if (FghConstants.ENV_DEV.equals(active)) {
            stdError.setDebugInfo(StdError.getDebugInfo(req, e));
        }
        return stdError;
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public StdError validation(HttpServletRequest req, Exception e) {
        logger.error(FghLog.getLog(Handler.VALIDATION.getName(), e.getMessage(), req));

        StdError stdError = StdError.of(sourceId.toUpperCase());
        if (e instanceof ConstraintViolationException) {
            StringBuilder builder = new StringBuilder();
            for (ConstraintViolation<?> error : ((ConstraintViolationException) e).getConstraintViolations()) {
                builder.append(error.getPropertyPath()).append(": ").append(error.getMessage()).append("; ");
            }
            stdError = StdError.of(sourceId.toUpperCase(), ErrorType.Business.name(), CommonErrorCode.VALIDATION, req.getRequestURI());
            stdError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            stdError.setMessage(builder.toString().trim());
        } else if (e instanceof MethodArgumentNotValidException) {
            StringBuilder builder = new StringBuilder();
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            BindingResult result = methodArgumentNotValidException.getBindingResult();
            final List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError error : fieldErrors) {
                builder.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }
            stdError = StdError.of(sourceId.toUpperCase(), ErrorType.Business.name(), CommonErrorCode.VALIDATION, req.getRequestURI());
            stdError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            stdError.setMessage(builder.toString().trim());
        }
        stdError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (FghConstants.ENV_DEV.equals(active)) {
            stdError.setDebugInfo(StdError.getDebugInfo(req, e));
        }
        return stdError;
    }

    @ExceptionHandler(FghException.class)
    public StdError dapBase(HttpServletRequest req, FghException e) {
        logger.error(FghLog.getLog(Handler.DAP_BASE.getName(), e.getMessage(), req), e);

        StdError stdError = StdError.of(sourceId.toUpperCase(), e, req.getRequestURI());
        stdError.setMessage(e.getMessage());
        if (FghConstants.ENV_DEV.equals(active)) {
            stdError.setDebugInfo(StdError.getDebugInfo(req, e));
        }
        return stdError;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public StdError unauthorized(HttpServletRequest req, FghException e) {
        logger.error(FghLog.getLog(Handler.UNAUTHORIZED.getName(), e.getMessage(), req), e);

        StdError stdError = StdError.of(sourceId.toUpperCase(), e, req.getRequestURI());
        stdError.setMessage(e.getMessage());
        if (FghConstants.ENV_DEV.equals(active)) {
            stdError.setDebugInfo(StdError.getDebugInfo(req, e));
        }
        return stdError;
    }

    enum Handler {
        UNEXPECTED("unexpected", "非预期系统异常"),
        NOT_FOUND("notFound", "API不存在"),
        DATA_ACCESS("dataAccess", "持久化层异常"),
        VALIDATION("validation", "参数校验异常"),
        DAP_BASE("dapBase", "中间件异常"),
        THIRD_CALL("thirdCall", "调用第三方异常"),
        UNAUTHORIZED("unauthorized", "权限验证异常");

        private final String id;
        private final String name;

        Handler(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
