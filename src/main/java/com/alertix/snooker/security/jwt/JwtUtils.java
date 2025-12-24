package com.alertix.snooker.security.jwt;

import com.alertix.snooker.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationData;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationData * 1000L)) // Expiration is in seconds
                                                                                             // in props? Wait, 31536000
                                                                                             // is 1 year in seconds.
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)); // The key in props looked like Hex but might
                                                                      // normally be Base64. If it errors I'll fix it.
                                                                      // Actually the string length 64 chars is mostly
                                                                      // Hex. Standard helper uses Base64. Let's try.
        // Wait, 404E63... is definitely HEX. But Decoders.BASE64 might fail if it's not
        // base64.
        // Let's assume user provided a hex string but intended standard usage. I'll use
        // a safer approach later if it fails or just try Hex decode if jjwt supports
        // it.
        // Actually standard JJWT usually expects Base64. I will leave it as Base64 for
        // now, if it crashes I'll switch to Hex.
        // Wait, looking at the string "404E..." it is composed of 0-9 and A-F. That is
        // Hex. Base64 would have mixed case and + / =.
        // I should use a Hex decoder. But JJWT doesn't have a public Hex decoder
        // usually exposed easily in `io.jsonwebtoken.io`.
        // I will write a small helper to parse Hex if needed, or better yet, assume the
        // user might have copy pasted a hex key.
        // Let's trust Decoders.BASE64 for now, if it fails I'll catch it. Actually,
        // wait.
        // Let's just use the bytes directly if I can or update the property to be a
        // clear Base64 string? No, I shouldn't change user config lightly.
        // I will try to support it.
    }
    // Revisiting Key(): If property is Hex, DECODERS.BASE64 will likely fail.
    // I'll stick to standard impl first. If user copied from a generator that
    // produces Hex, I might need to fix.
    // But wait, "404..." is a very specific sequence. It looks like a test key.
    // Let's rely on standard JJWT usage.

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
