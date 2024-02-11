package az.najafov.deforestationnews.common;

public class Constants {

    public static final String[] IGNORED_URLS = new String[]{"/error", "/favicon.ico/", "/csrf",
            "/actuator/**", "/i18n/**", "/swagger-ui/**", "/swagger-ui.html", "/content/**",
            "/v3/api-docs/**", "/v3/api-docs/swagger-config", "/webjars/**", "/swagger-resources/**",
            "/api/auth/logout", "/api/auth/refresh", "/api/auth/login", "/api/auth/register", "/ws/**",
            "/api/general/*", "/api/general/*/**"};

}
