package AUTHAPP.demo.Security.JWT;

import AUTHAPP.demo.Security.Services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${auth.app.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
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
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }
        catch (SignatureException e) {
            logger.error("INVALID JWT SIGNATURE : {}" + e.getMessage());
        }
        catch (MalformedJwtException e) {
            logger.error("INVALID JWT TOKEN : {}" + e.getMessage());
        }
        catch (ExpiredJwtException e) {
            logger.error("JWT TOKEN IS EXPIRED : {}" + e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            logger.error("JWT TOKEN IS UNSUPPORTED : {}" + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            logger.error("JWT CLAIMS STRING IS EMPTY : {}" + e.getMessage());
        }

        return false;
    }

}
