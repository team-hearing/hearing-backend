package com.hearing_backend.domain.user.config;

import com.hearing_backend.domain.user.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        String path = request.getRequestURI();

        // 회원가입, 로그인 요청 경로는 토큰 검사 없이 바로 통과
        if (path.equals("/api/auth/register") || path.equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

     
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            if (jwtService.validateToken(token)) {
                username = jwtService.getUsernameFromToken(token);
            }
        }
        
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtService.getRoleFromToken(token); 
            
            //Spring Security는 권한 체크 시 앞에 "ROLE_" 접두사가 붙은 값**을 요청.
            String springRole = "ROLE_" + role;
            
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> " + springRole);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority(springRole)));
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}