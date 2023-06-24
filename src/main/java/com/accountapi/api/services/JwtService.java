package com.accountapi.api.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${account.app.jwtSecret}")
    private  String SigningKey;
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    HttpServletRequest httpServletRequest;
    public String extractUsername(String jwt){
        return extractClaim(jwt , Claims::getSubject);
    }

    public String generateToken(
            Map<String , Object> extraClaims,
            UserDetails userDetails)
    {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000 ))
                .signWith(getSigningKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>() , userDetails);
    }

    public boolean isTokenValid(String token , UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }

    public <T>T extractClaim(String token , Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            httpServletRequest.setAttribute("errorCode", "401");
            httpServletRequest.setAttribute("errorMessage", "Invalid JWT signature");
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            httpServletRequest.setAttribute("errorCode", "401");
            httpServletRequest.setAttribute("errorMessage", "Invalid JWT token");

        } catch (ExpiredJwtException e) {
            logger.info("JWT token is expired: {}", e.getMessage());
            httpServletRequest.setAttribute("errorCode", "401");
            httpServletRequest.setAttribute("errorMessage", "JWT token is expired");
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            httpServletRequest.setAttribute("errorCode", "401");
            httpServletRequest.setAttribute("errorMessage", "Unsupported jwt ");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            httpServletRequest.setAttribute("errorCode", "401");
            httpServletRequest.setAttribute("errorMessage", "JWT claims string is empty ");

        }

        return false;
    }
}
