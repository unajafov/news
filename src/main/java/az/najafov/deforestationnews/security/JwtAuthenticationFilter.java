package az.najafov.deforestationnews.security;

import az.najafov.deforestationnews.common.ErrorHandlerUtil;
import az.najafov.deforestationnews.exception.BaseErrorResponse;
import az.najafov.deforestationnews.security.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info(httpServletRequest.getRequestURI());
        try {
            final String jwtToken = jwtTokenProvider.extractToken(httpServletRequest);
            Authentication authentication = jwtTokenProvider
                    .parseAuthentication(jwtToken, httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("An error occurred:", e);
            if (e instanceof JwtException) {
                if (e instanceof ExpiredJwtException) {
                    ErrorHandlerUtil.buildHttpErrorResponse(response, new BaseErrorResponse("JWT expired", "AUTH_001",
                            "JWT expired"), 401);
                    return;
                }
                ErrorHandlerUtil.buildHttpErrorResponse(response, new BaseErrorResponse("JWT wrong", "AUTH_002",
                        "JWT wrong"), 401);
                return;
            }
            chain.doFilter(request, response);
        }
    }

}
