/*
package az.najafov.deforestationnews.security;

import az.najafov.deforestationnews.common.ErrorHandlerUtil;
import az.najafov.deforestationnews.common.SecurityUtil;
import az.najafov.deforestationnews.exception.BaseErrorResponse;
import az.najafov.deforestationnews.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class AuthorizationFilter extends GenericFilterBean {

    private final SecurityUtil securityUtil;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AuthorizationFilter(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            String URI = httpServletRequest.getRequestURI();
            if (URI.startsWith("/api/admin")) {
                User user = securityUtil.getUser();
                boolean hasAdminRole = hasAdminRole(user.getRoles());
                if (!hasAdminRole) {
                    BaseErrorResponse baseErrorResponse = new BaseErrorResponse(
                            "Permission denied", "AUTH_003",
                            "This url " + URI + " cannot be accessed by " + user.getUsername());
                    ErrorHandlerUtil.buildHttpErrorResponse(response, baseErrorResponse, 403);
                }
            }
            chain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Authorization filter error", e);
            e.printStackTrace();
            BaseErrorResponse baseErrorResponse = new BaseErrorResponse("Exception", "AUTH_006",
                    "Filter chain exception");
            ErrorHandlerUtil.buildHttpErrorResponse(response, baseErrorResponse, 500);
        }
    }

    private boolean hasAdminRole(Set<Role> roles) {
        for (Role role : roles) {
            if ("ADMIN".equals(role.getName())) {
                return true;
            }
        }
        return false;
    }

}

*/
