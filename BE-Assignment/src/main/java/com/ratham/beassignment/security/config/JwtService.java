package com.ratham.beassignment.security.config;

import com.ratham.beassignment.model.SchoolUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {

    String KEY = "RSWAZGDFCXGJHNVJ67GHJBVHGkjhbnthuyf84561384541698478fsdgrfSRTRGsfvcs4rrgfrg5595RSWAZGDFCXGJHNVJ67GHJBVHGkjhbnt";

    public String generateJwtToken(String id) {
        Date currentTs = Date.from(Instant.now());
        Date expireTs = Date.from(Instant.now()
                .plusSeconds(TimeUnit.MINUTES.toSeconds(5)));

        return Jwts.builder()
                .id(id)
                .subject("login")
                .issuedAt(currentTs)
                .expiration(expireTs)
                .signWith(getSignatureKey())
                .compact();
    }

    private Key getSignatureKey() {
        return Keys.hmacShaKeyFor(getSignatureKeyBytes());
    }

    private byte[] getSignatureKeyBytes() {
        return KEY.getBytes(StandardCharsets.UTF_8);
    }

    public boolean validateJwtToken(String token, int userId) {

        String userIdStr = getIdFromToken(token);

        try {
            int tokenUserId = Integer.parseInt(userIdStr);
            if (userId == tokenUserId) {
                return !isTokenExpired(token);
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String getIdFromToken(String token) {
        return getClaimsFromToken(token, Claims::getId);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims jwtClaims = getAllClaimsFromToken(token);
        return claimsResolver.apply(jwtClaims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
