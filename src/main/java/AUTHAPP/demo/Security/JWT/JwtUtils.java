package AUTHAPP.demo.Security.JWT;

import AUTHAPP.demo.Security.Services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${auth.app.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("roles", userPrincipal.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            decodeToken(authToken);
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }
        catch (SignatureException e) {
            e.printStackTrace();
            logger.error("INVALID JWT SIGNATURE : {}" + e.getMessage());
        }
        catch (MalformedJwtException e) {
            e.printStackTrace();
            logger.error("INVALID JWT TOKEN : {}" + e.getMessage());
        }
        catch (ExpiredJwtException e) {
            e.printStackTrace();
            logger.error("JWT TOKEN IS EXPIRED : {}" + e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            e.printStackTrace();
            logger.error("JWT TOKEN IS UNSUPPORTED : {}" + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error("JWT CLAIMS STRING IS EMPTY : {}" + e.getMessage());
        }

        return false;
    }

    private void decodeToken(String jwtToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody();

        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        System.out.println("USERNAME: " + username + ", ROLES: " + roles);
    }

}
