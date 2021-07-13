package com.fobgochod.constant;

/**
 * 系统常量
 *
 * @author zhouxiao
 * @date 2021/7/13
 */
public class FghConstants {

    public static final String HTTP_HEADER_ACCESS_TOKEN_KEY = "digi-middleware-auth-access";
    public static final String HTTP_HEADER_DRIVE_INFO_KEY = "digi-middleware-drive-access-info";

    public static final String HTTP_HEADER_USER_TOKEN_KEY = "digi-middleware-auth-user";
    public static final String HTTP_HEADER_USER_INFO_KEY = "digi-middleware-auth-user-info";

    public static final String HTTP_HEADER_TENANT_KEY = "digi-middleware-auth-tenant";

    public static final String HTTP_HEADER_API_ARG_KEY = "digi-middleware-drive-arg";

    public static final String DEFAULT_TENANT = "default";

    public static final String ANONYMOUS_USER = "anonymousUser";

    public static final char AT = '@';
    public static final char DOT = '.';
    public static final char TAB = '\t';
    public static final char DASH = '-';
    public static final char COLON = ':';
    public static final char COMMA = ',';
    public static final char SLASH = '/';
    public static final char DOLLAR = '$';
    public static final char PERCENT = '%';
    public static final char ESCAPE = '\\';
    public static final char ASTERISK = '*';
    public static final char SEMICOLON = ';';
    public static final char CURLY_LEFT = '{';
    public static final char UNDERSCORE = '_';
    public static final char CURLY_RIGHT = '}';
    public static final char DOUBLE_QUOTE = '"';
    public static final char SINGLE_QUOTE = '\'';
    public static final char LEFT_PARENTHESIS = '(';
    public static final char RIGHT_PARENTHESIS = ')';

    public static final String EMPTY = "";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String DEFAULT_VALUE_SEPARATOR = ":-";
    public static final String DEFAULT_CONTEXT_NAME = "default";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String LOCALE = "Locale";

    public static final String ENV_DEV = "dev";
    public static final String ENV_PROD = "prod";
    public static final String ENV_TEST = "test";
}
