package az.najafov.deforestationnews.security.provider;

import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${app.secret-key}")
    private String SECRET_KEY;

    @Value("${app.jwt-authentication-token-expires-minutes}")
    private Integer authenticationTokenExpiresMinutes;

    public String extractEmail(String token) throws JwtException {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isChangePasswordRequired(String token) throws JwtException {
        Claims claims = extractAllClaims(token);
        return (boolean) claims.get("changePasswordRequired");
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("username").toString();
    }

    public boolean isUserChangedPassword(String token) {
        Claims claims = extractAllClaims(token);
        return (boolean) claims.get("passwordChanged");
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String createToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authorities = "";
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (!authorities.equals(""))
                authorities = authorities.concat(",");
            authorities = authorities.concat(authority.getAuthority());
        }
        return Jwts.builder()
                .claim("username", userDetails.getUsername())
                .claim("roles", authorities)
                .setIssuedAt(new Date())
                .setIssuer(userDetails.getUsername())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(authenticationTokenExpiresMinutes).toInstant()))
                .compact();
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        String authorities = "";
        for (Role authority : user.getRoles()) {
            if (!authorities.equals(""))
                authorities = authorities.concat(",");
            authorities = authorities.concat(authority.getName());
        }
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("username", user.getUsername())
                .claim("roles", authorities)
                .setIssuer(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(authenticationTokenExpiresMinutes).toInstant()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails generalUser) {
        try {
            final String email = extractEmail(token);
            boolean isTokenValid = (email.equals(generalUser.getUsername()) && !isTokenExpired(token));

            return isTokenValid;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return !claims.getExpiration().before(new Date());
    }

    private boolean isTokenExpired(String token) throws JwtException {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws JwtException {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) throws JwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public Key getSignInKey() {
        byte[] secret = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(secret);

    }

    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
