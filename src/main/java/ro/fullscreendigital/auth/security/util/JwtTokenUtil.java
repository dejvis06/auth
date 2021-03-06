package ro.fullscreendigital.auth.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import ro.fullscreendigital.auth.security.UserCustody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    public String generateToken(UserCustody userCustody) {
        return Jwts.builder().setClaims(getClaimsFromUser(userCustody)).setSubject(userCustody.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Map<String, Object> getClaimsFromUser(UserCustody userCustody) {

        Map<String, Object> authorities = new HashMap<>();
        for (GrantedAuthority grantedAuthority : userCustody.getAuthorities()) {
            authorities.put(grantedAuthority.getAuthority(), grantedAuthority);
        }
        return authorities;
    }

    public Boolean isTokenValid(String token, String username) {
        final String usernameToken = getUsername(token);
        return (usernameToken.equals(username) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public List<GrantedAuthority> getAuthorities(String token) {

        Map<String, Object> claims = getAllClaims(token);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        claims.entrySet().forEach(claim -> {
            grantedAuthorities.add((GrantedAuthority) claim.getValue());
        });
        return grantedAuthorities;
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities,
                                            HttpServletRequest httpRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

        return usernamePasswordAuthenticationToken;
    }
}
