package com.aiqfome.demo.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JWTUtils {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 300; // 5 horas

    public static String gerarToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public static String getEmailPeloToken(String token) {
        return validarToken(token)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private static JwtParser validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }
}
