package com.fobgochod.constant;

/**
 * @author chenxsa
 * @date: 2018-6-6 19:21
 * @Description:
 */
public class FghConstants {

    /**
     * 在HttpHeader中传递的IAM Token信息
     */
    public static final String HTTP_HEADER_ACCESS_TOKEN_KEY = "digi-middleware-auth-access";

    /**
     * 在HttpHeader中传递的User Token信息
     */
    public static final String HTTP_HEADER_USER_TOKEN_KEY = "digi-middleware-auth-user";

    public static final String HTTP_HEADER_USER_INFO_KEY = "digi-middleware-auth-use-info";

    /**
     * 在HttpHeader中传递的租户信息
     */
    public static final String HTTP_HEADER_TENANT_KEY = "digi-middleware-auth-tenant";

    /**
     * 在HttpHeader中传递的Token信息
     */
    public static final String HTTP_HEADER_DRIVE_TOKEN_KEY = "digi-middleware-drive-access";

    public static final String HTTP_HEADER_DRIVE_INFO_KEY = "digi-middleware-drive-access-info";

    /**
     * 在HttpHeader中传递的请求参数
     */
    public static final String HTTP_HEADER_API_ARG_KEY = "digi-middleware-drive-arg";


    /**
     * 默认Bucket
     */
    public static final String DEFAULT_STORAGE = "mongoDefault";

    /**
     * 个人文档
     */
    public static final String PERSONAL_DOCUMENT_COLLECTION = "MyDocument";

    /**
     * FileInfo 相关常量
     */
    public static final String FILE_INFO_COLLECTION_NAME = "FileInfo";

    /**
     * 在Http请求参数中传递，租户ID
     */
    public static final String HTTP_HEADER_TENANTID_KEY = "tenantId";

    /**
     * 默认租户
     */
    public static final String DEFAULT_TENANT = "default";

    /**
     * 匿名用户
     */
    public static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * DMC管理员
     */
    public static final String ADMIN = "admin";

    @Deprecated
    public static final String CONTENT_LENGTH = "ContentLength";
}
