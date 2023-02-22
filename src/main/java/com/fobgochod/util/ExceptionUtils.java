package com.fobgochod.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fobgochod.domain.base.I18nHandler;
import com.fobgochod.domain.base.StdError;
import com.fobgochod.domain.enumeration.ErrorType;
import com.fobgochod.exception.FghException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 功能描述
 *
 * @author seven
 * @date 2020/6/7
 */
public class ExceptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);
    private static final ObjectMapper objectMapper = JsonUtils.createObjectMapper();

    public static void writeUnAuth(HttpServletRequest request, HttpServletResponse response, I18nHandler i18nHandler) {
        StdError stdError = StdError.of();
        stdError.setType(ErrorType.System);
        stdError.setCode(i18nHandler.getCode());
        stdError.setMessage(i18nHandler.getMessage());
        stdError.setPath(request.getRequestURI());
        println(response, HttpServletResponse.SC_UNAUTHORIZED, stdError);

        logger.error(stdError.toString());
    }

    public static void writeUnAuth(HttpServletRequest request, HttpServletResponse response, FghException e) {
        StdError stdError = StdError.of();
        stdError.setType(e.getErrorType());
        stdError.setCode(e.getHandler().getCode());
        stdError.setMessage(e.getHandler().getMessage());
        stdError.setPath(request.getRequestURI());
        println(response, HttpServletResponse.SC_UNAUTHORIZED, stdError);

        logger.error(stdError.toString());
    }

    private static void println(HttpServletResponse response, int code, Object data) {
        try (PrintWriter writer = response.getWriter();) {
            response.setStatus(code);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.println(objectMapper.writeValueAsString(data));
        } catch (IOException ioe) {
            logger.error("IOException {}", ioe.getMessage());
        }
    }
}
