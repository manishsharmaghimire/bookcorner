package com.bookcorner.auth.security;

import com.bookcorner.auth.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Read Authorization Header
        final String authHeader = request.getHeader("Authorization");

        // 2. If no JWT, continue request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract JWT
        final String jwt = authHeader.substring(7);

        final String username;

        try {

            // 4. Extract username from JWT
            username = jwtService.extractUsername(jwt);

        } catch (ExpiredJwtException e) {

            sendUnauthorized(response, "JWT token has expired.");
            return;

        } catch (MalformedJwtException e) {

            sendUnauthorized(response, "Malformed JWT token.");
            return;

        } catch (UnsupportedJwtException e) {
  e.printStackTrace();
            sendUnauthorized(response, "Unsupported JWT token.");
            return;

        } catch (SignatureException e) {

            sendUnauthorized(response, "Invalid JWT signature.");
            return;

        } catch (Exception e) {

            sendUnauthorized(response, "Invalid JWT token.");
            return;
        }

        // 5. Authenticate only if user is not already authenticated
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // 6. Validate JWT
            if (jwtService.isTokenValid(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);

            } else {

                sendUnauthorized(
                        response,
                        "Token is invalid for this user."
                );
                return;
            }
        }

        // 7. Continue to next filter
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write("""
                {
                    "error": "%s"
                }
                """.formatted(message));
    }
}