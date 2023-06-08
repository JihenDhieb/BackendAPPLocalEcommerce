package com.example.AppEcommerce.Security.jwt;


import com.example.AppEcommerce.Model.User;
import com.example.AppEcommerce.Repository.UserRepository;
import com.example.AppEcommerce.Security.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Autowired
    UserRepository userRepository;
    @Value("123456")
    private String jwtSecret;
    @Value("1234")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        String subject = "";
        if (user.isPresent()) {
            if (user.get().getEmail() != null) {
                subject = user.get().getEmail();
            }
            return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + 8400000)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
        } else return "";
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}