package com.bunyaminkalkan.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.secret}")
    private String APP_SECRET;
    @Value("${expires.in}")
    private Long EXPIRES_IN;

    public String generateJwtToken(Authentication auth){
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder().setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, APP_SECRET).compact();
    }

    public Long getUserIdFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(APP_SECRET).build().parseSignedClaims(token).getPayload();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(APP_SECRET).build().parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException |
                 MalformedJwtException e) {
            return false;
        }

    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(APP_SECRET).build().parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }
}
