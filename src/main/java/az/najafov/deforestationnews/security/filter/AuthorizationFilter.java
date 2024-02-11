package az.najafov.deforestationnews.security.filter;

import az.najafov.deforestationnews.common.ErrorHandlerUtil;
import az.najafov.deforestationnews.common.SecurityUtil;
import az.najafov.deforestationnews.exception.BaseErrorResponse;
import az.najafov.deforestationnews.model.Role;
import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.model.IgnoredPermission;
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
            User user = securityUtil.getUser();
            Set<Role> roles = user.getRoles();

            if (Objects.isNull(roles) || roles.isEmpty()) {
                BaseErrorResponse baseErrorResponse = new BaseErrorResponse(
                        "Permission denied", "AUTH_003",
                        user.getUsername() + " does not have any roles");
                ErrorHandlerUtil.buildHttpErrorResponse(response, baseErrorResponse, 403);
                return;
            }

            String URI = httpServletRequest.getRequestURI();
            String httpMethod = httpServletRequest.getMethod();
            for (IgnoredURI ignoredURI : getAllowedURIs(roles)) {
                if (antPathMatcher.match(ignoredURI.URI, URI) && ignoredURI.httpMethod.equals(httpMethod)) {
                    BaseErrorResponse baseErrorResponse = new BaseErrorResponse(
                            "Permission denied", "AUTH_003",
                            "This url " + URI + " cannot access by " + user.getUsername());
                    ErrorHandlerUtil.buildHttpErrorResponse(response, baseErrorResponse, 403);
                    return;
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

    private Set<IgnoredURI> getAllowedURIs(Set<Role> roles) {
        Set<IgnoredURI> ignoredURIs = new HashSet<>();
        for (Role role : roles) {
            for (IgnoredPermission ignoredPermission : role.getIgnoredPermissions()) {
                ignoredURIs.add(new IgnoredURI(ignoredPermission.getUrl(), ignoredPermission.getHttpMethod()));
            }
        }
        return ignoredURIs;
    }

    private class IgnoredURI {
        private final String URI;
        private final String httpMethod;

        public IgnoredURI(String URI, String httpMethod) {
            this.URI = URI;
            this.httpMethod = httpMethod;
        }

    }

}

