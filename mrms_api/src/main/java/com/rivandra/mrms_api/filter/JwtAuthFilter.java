package com.rivandra.mrms_api.filter;

import com.rivandra.mrms_api.service.ApiAuthorizationService;
import com.rivandra.mrms_api.service.JwtService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService _jwtService;

    @Autowired
    private final ApiAuthorizationService _apiAuthorizationService;
    
    public JwtAuthFilter(JwtService jwtService, ApiAuthorizationService apiAuthorizationService) {
        _jwtService = jwtService;
        _apiAuthorizationService = apiAuthorizationService;
    }

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {

        String[] whiteList = new String[]{"/auth/login"};
        String requestPath = request.getRequestURI();

        boolean isWhiteListed = Arrays.stream(whiteList).anyMatch(requestPath::equals);
        if (isWhiteListed) {
            filterChain.doFilter(request, response);
            return;
        }

        Enumeration<String> headers = request.getHeaderNames();
        final String authHeader = request.getHeader("Authorization");
        final String jwtFromCookie = getJwtToken(request);

        String jwtTokenStr = null;
        if (authHeader != null && !authHeader.startsWith("bearer ")) {
           jwtTokenStr = authHeader.substring(7);
        } 
        if (jwtFromCookie != null) {
            jwtTokenStr = jwtFromCookie;
        }

        if(jwtTokenStr == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing or invalid.");
            return;
        }

        boolean isValid = _jwtService.validateToken(jwtTokenStr);
        if(!isValid) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid.");
            return;
        }

        String userRole = _jwtService.extractClaim(jwtTokenStr, "role", String.class);

        // Get the HTTP method (endpoint type)
        String endpointType = request.getMethod(); // GET, POST, DELETE, etc.

        // Get the request URI (path)
        String path = request.getRequestURI();

        boolean isAccessGranted = _apiAuthorizationService.isAccessGranted(endpointType, path, userRole);

        if (!isAccessGranted) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Get the user's roles from the token

    // private String[] getAllowedRolesFromAspect(HttpServletRequest request) {
    //     // Retrieve the handler method
    //     HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        
    //     if (handlerMethod != null) {
    //         // Get the @RolesAllowed annotation
    //         RolesAllowed rolesAllowed = handlerMethod.getMethodAnnotation(RolesAllowed.class);
            
    //         if (rolesAllowed != null) {
    //             // Return the allowed roles
    //             return rolesAllowed.value();
    //         }
    //     }
        
    //     // Return an empty array if no @RolesAllowed is found
    //     return new String[0];
    // }
    
    
}
