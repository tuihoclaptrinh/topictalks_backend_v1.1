package com.anonymity.topictalks.filters;

import com.anonymity.topictalks.services.IJwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.filters
 * - Created At: 15-09-2023 15:14:37
 * @since 1.0 - version of class
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    /** implementation is provided in config.ApplicationSecurityConfig */
    private final UserDetailsService userDetailsService;
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if(authHeader ==  null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // after "Bearer "
        final String username =jwtService.extractUserName(jwt);

        /*
           SecurityContextHolder: is where Spring Security stores the details of who is authenticated.
           Spring Security uses that information for authorization.*/

        if(StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if(jwtService.isTokenValid(jwt, userDetails)){

                    //update the spring security context by adding a new UsernamePasswordAuthenticationToken
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);

                    SecurityContextHolder.setContext(context);
                }

            } catch (Exception ex) {
                //this is very important, since it guarantees the user is not authenticated at all
                filterChain.doFilter(request, response);
                SecurityContextHolder.clearContext();
                return;
            }

        }

        filterChain.doFilter(request,response);
    }

}
