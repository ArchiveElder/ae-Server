package com.ae.community.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

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


    // Token 내용을 뜯어서 userIdx, nickname,
    public String getUserIdx(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userIdx", String.class);
        }catch (Exception ex) {
            ex.printStackTrace();
            log.error("*** token 내용에 에러가 있음 -- JwtProvider");
            return "INVALID JWT-USERIDX";
        }
    }
    public String getNickname(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("nickname", String.class);
        }catch (Exception ex) {
            ex.printStackTrace();
            log.error("*** token 내용에 에러가 있음 -- JwtProvider");
            return "INVALID JWT-NICKNAME";
        }
    }
    public String getIcon(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("icon", String.class);
        }catch (Exception ex) {
            ex.printStackTrace();
            log.error("*** token 내용에 에러가 있음 -- JwtProvider");
            return "INVALID JWT-ICON";
        }
    }

}
