package com.instacopy.instacopy.security;

import com.instacopy.instacopy.entity.User;
import com.instacopy.instacopy.service.IUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.mapping.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    JWTTokenProvider jwtTokenProvider;
    @Autowired
    IUserDetailsService iUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJWTfromRequest(request);
        try {
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
                Long id = jwtTokenProvider.getUserIdFromToken(jwt);
                User user = iUserDetailsService.loadUserById(id);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,null, Collections.emptyList()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception exception){
            LOG.error(exception.getMessage());
        }

        filterChain.doFilter(request,response);
    }

    private String getJWTfromRequest(HttpServletRequest request){
        String bearToken = request.getHeader(SecurityConstans.HEADER_STRING);
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstans.TOKEN_PREFIX)){
            return bearToken.split(" ")[1];
        }
        return null;
    }
}
