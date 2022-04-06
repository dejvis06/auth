package ro.fullscreendigital.auth.security.util;

public class SecurityConstant {

    public static final long EXPIRATION_TIME = 432_000_000; // 5 days in milliseconds

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String JWT_TOKEN_HEADER = "Jwt-Token";

    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";

    public static final String GET_ARRAYS_LLC = "Get Arrays, LLC";

    public static final String AUTHORITIES = "Authorities";

    public static final String FORBIDDEN_MESSAGE = "Log in is required to access this page";

    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";

    public static final String OPTIONS_HTTP_METHOD = "Options";

    public static final String[] PUBLIC_URLS = {"/", "/oauth2/**", "/user/login", "/user/register"};

    // public static final String[] PUBLIC_URLS = { "**" };
}
