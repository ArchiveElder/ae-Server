package com.ae.community.config.security;

import com.ae.community.exception.chaebbiException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtProvider jwtProvider;

    // request로 들어오는 Jwt의 유효성을 검증 - JwtProvider.validationToken()을 필터로서 FilterChain에 추가
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);
            log.info("Filter is running...");
            log.info("token: " + token);
            if (token != null && !token.equalsIgnoreCase("null")) {
                String userIdx = jwtProvider.getUserIdx(token);
                String nickname = jwtProvider.getNickname(token);
                String icon = jwtProvider.getIcon(token);
                if(userIdx.equals("INVALID JWT-USERIDX") || nickname.equals("INVALID JWT-NICKNAME") || icon.equals("INVALID JWT-ICON")) {
                    log.info("*** This token is invalid! *** ");
                }
                log.info("Authenticated user ID : " + userIdx );

                HashMap<String,String> user = new HashMap<>();
                user.put("userIdx", userIdx);
                user.put("icon", icon);
                user.put("nickname", nickname);

                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);

            }
            if(token ==null) {
                log.info("** token is null, Please check ");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
