package org.example.security;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    private final SecretKey secretKey;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   UserDetailsService userDetailsService) {
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(String email) {
        return buildToken(email, expiration, ACCESS_TOKEN_TYPE);
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, refreshExpiration, REFRESH_TOKEN_TYPE);
    }

    private String buildToken(String email, long expTime, String type) {
        Date now = new Date();
        return Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE_CLAIM, type)
                .issuer(issuer)
                .audience()
                .add(audience)
                .and()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expTime))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(issuer)
                    .requireAudience(audience)
                    .build()
                    .parseSignedClaims(token);

            Date expirationDate = claimsJws
                    .getPayload()
                    .getExpiration();

            return expirationDate != null
                    && expirationDate.after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getTokenType(String token) {
        return getClaimFromToken(token, claims -> claims.get(
                TOKEN_TYPE_CLAIM, String.class));
    }

    public boolean isRefreshToken(String token) {
        return REFRESH_TOKEN_TYPE.equals(getTokenType(token));
    }

    public boolean isAccessToken(String token) {
        return ACCESS_TOKEN_TYPE.equals(getTokenType(token));
    }

    private <T> T getClaimFromToken(String token,
                                    Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(issuer)
                .requireAudience(audience)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
