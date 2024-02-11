package az.najafov.deforestationnews.security.provider;

import az.najafov.deforestationnews.common.ErrorHandlerUtil;
import az.najafov.deforestationnews.exception.LongTimeInActiveUserException;
import az.najafov.deforestationnews.exception.TokenIsNotExpiredException;
import az.najafov.deforestationnews.exception.TokenNotValidException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.Authentication;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${security.jwt.base64-secret}")
    private String SECRET_KEY;
    @Value("${security.jwt.authentication-token-expires-minutes}")
    private Long EXPIRATION_MINUTE;
    private final UserDetailsService userDetailsService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, org.springframework.security.core.userdetails.UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * EXPIRATION_MINUTE)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            token = httpServletRequest.getParameter("token");
            if (token == null) {
                throw new TokenNotValidException("Token is null");
            } else
                return token;
        }
        if (!token.startsWith("Bearer")) {
            throw new TokenNotValidException("Token not starts with Bearer");
        }
        return token.substring(7);
    }

    public Authentication parseAuthentication(String token, HttpServletRequest httpServletRequest) {
        String username = extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        return authToken;
    }

    public String createRefreshToken(String expiredToken) {
        try {
            extractAllClaims(expiredToken);
            throw new TokenIsNotExpiredException("Token isn't expired. Pls dont force to refresh!");
        } catch (ExpiredJwtException ex) {
            Date tokenValidity = new Date(System.currentTimeMillis() + (1000 * 60 * EXPIRATION_MINUTE));
            if (new Date().getTime() > (ex.getClaims().getExpiration().getTime() + 15 * 60 * 1000))
                throw new LongTimeInActiveUserException("Long time in active");
            Claims claims = ex.getClaims();
            return Jwts.builder()
                    .setSubject((String) claims.get("sub"))
                    .claim("username", claims.get("username"))
                    .claim("roles", claims.get("roles"))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                    .setExpiration(tokenValidity)
                    .compact();
        }
    }

}
