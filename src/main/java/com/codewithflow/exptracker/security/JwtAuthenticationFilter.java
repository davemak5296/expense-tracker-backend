package com.codewithflow.exptracker.security;

import com.codewithflow.exptracker.repository.IssuedJwtRepository;
import com.codewithflow.exptracker.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final IssuedJwtRepository issuedJwtRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            IssuedJwtRepository issuedJwtRepository
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.issuedJwtRepository = issuedJwtRepository;
    }

    static class AppendRequest extends HttpServletRequestWrapper {
        private final Map<String, String> customParameters;

        public AppendRequest(ServletRequest request) {
            super((HttpServletRequest) request);
            customParameters = new HashMap<>();
        }

        public void addCustomParameter(String name, String value) {
            customParameters.put(name, value);
        }

        @Override
        public String getParameter(String name) {
            String originalParameter = super.getParameter(name);
            if (originalParameter != null) {
                return originalParameter;
            }
            return customParameters.get(name);
        }
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String[] whiteList = {"/register", "/registerConfirm", "login", "/refreshToken"};
        if (Arrays.stream(whiteList).anyMatch(request.getServletPath()::contains)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = null;
        String userEmail;

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JWT_ACCESS_TOKEN")) {
                jwt = cookie.getValue();
            }
        }

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isTokenValid = issuedJwtRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (jwtService.isTokenValid(jwt) && isTokenValid) {

            userEmail = jwtService.extractUsername(jwt);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        AppendRequest customReq = new AppendRequest(request);
        customReq.addCustomParameter("jwt_user_id", jwtService.extractUserId(jwt));
        filterChain.doFilter(customReq, response);
    }
}
