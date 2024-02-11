package az.najafov.deforestationnews.security.filter;

import az.najafov.deforestationnews.common.ErrorHandlerUtil;
import az.najafov.deforestationnews.exception.BaseErrorResponse;
import az.najafov.deforestationnews.security.userdetails.CustomUserDetailsService;
import az.najafov.deforestationnews.security.userdetails.UserDetails;
import az.najafov.deforestationnews.security.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = tokenProvider.getJWTFromRequest(request);
            if (StringUtils.hasText(token) && tokenProvider.isTokenValid(token)) {
                String username = tokenProvider.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, token, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                logger.error("Cannot set user authentication");
                BaseErrorResponse baseErrorResponse = new BaseErrorResponse(
                        "Permission denied", "AUTH_003",
                        "Full authentication needed");
                ErrorHandlerUtil.buildHttpErrorResponse(response, baseErrorResponse, 401);
            }

        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            BaseErrorResponse baseErrorResponse = new BaseErrorResponse(
                    "Permission denied", "AUTH_003",
                    "Full authentication needed");
            ErrorHandlerUtil.buildHttpErrorResponse(response, baseErrorResponse, 401);
        }
    }

}
