package com.ae.ae_SpringServer.config.security;

import com.ae.ae_SpringServer.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component

public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private Long tokenValidMillisecond = 60 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 생성 : 페이로더
    public String createToken(User user) {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setSubject(user.getId().toString())
                .setSubject(Integer.toString(user.getIcon()))
                .setSubject(user.getNickname())
                .setIssuer("app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }
    // Token 내용을 뜯어서 id 얻기
    public String validateAndGetUserId(String token) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("*** token 내용에 에러가 있음 -- JwtProvide");
            return "INVALID JWT";
        }

    }


    /*
       // jwt의 유효성 및 만료일자 확인
    public boolean validationToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date()); // 만료날짜가 현재보다 이전이면 false
        } catch (Exception e) {
            return false;
        }
    }

     */
}
