package com.fobgochod.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.I18nHandler;
import com.fobgochod.domain.base.StdError;
import com.fobgochod.domain.enumeration.ErrorType;
import com.fobgochod.exception.FghException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static String getExceptionDetail(Exception e) {
        if (e == null) {
            return "";
        }
        StringBuilder msg = new StringBuilder("");
        String message = e.toString();
        int length = e.getStackTrace().length;
        if (length > 0) {
            msg.append(message).append(System.getProperty("line.separator"));
            for (int i = 0; i < length; i++) {
                msg.append(e.getStackTrace()[i]).append(System.getProperty("line.separator"));
            }
        } else {
            msg.append(message);
        }
        return msg.toString().trim();
    }

    public static String getStackTrace(Exception e) {
        StringBuilder builder = new StringBuilder(e.toString());
        StackTraceElement[] steArray = e.getStackTrace();
        for (StackTraceElement ste : steArray) {
            builder.append(FghConstants.LINE_SEPARATOR);
            builder.append("at ").append(ste.toString());
        }
        return builder.toString();
    }

    public static String getErrorAsString(Exception e) {
        String responseBody;
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException clientError = (HttpStatusCodeException) e;
            responseBody = clientError.getResponseBodyAsString();
        } else {
            responseBody = e.getMessage();
        }
        return responseBody;
    }

    public static Object getError(Exception e) {
        String responseBody = getErrorAsString(e);
        Object object = null;
        try {
            object = objectMapper.readValue(responseBody, Object.class);
        } catch (Exception ignored) {
        }
        return object;
    }

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


    public static void println(HttpServletResponse response, StdError stdError) {
        println(response, response.getStatus(), stdError);
    }

    private static void println(HttpServletResponse response, int code, Object data) {
        try (PrintWriter writer = response.getWriter();) {
            response.setStatus(code);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            writer.println(objectMapper.writeValueAsString(data));
        } catch (IOException ioe) {
            logger.error("IOException {}", ioe.getMessage());
        }
    }
}
