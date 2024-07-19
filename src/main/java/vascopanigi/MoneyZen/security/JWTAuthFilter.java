package vascopanigi.MoneyZen.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.exceptions.UnauthorizedException;
import vascopanigi.MoneyZen.payloads.ErrorsDTO;
import vascopanigi.MoneyZen.services.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    public JWTAuthFilter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Problems with your token.");
        String accessToken = authHeader.substring(7);
        jwtTools.verifyToken(accessToken);
        String utentiId = jwtTools.extractIdFromToken(accessToken);
        User currentUser = userService.findById(UUID.fromString(utentiId));
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            ErrorsDTO errorDTO = new ErrorsDTO(e.getMessage(), LocalDateTime.now());
            String jsonResponse = objectMapper.writeValueAsString(errorDTO);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
