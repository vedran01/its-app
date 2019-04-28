package com.vedran.itsapp.security;

import com.vedran.itsapp.model.ItsUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;


@Component
public class ItsJwtHelper {

  private Key secret;

  String generateAuthorizationToken(ItsUser user){
    Date exp = calculateExpirationDate(1, ChronoUnit.DAYS);
    return Jwts.builder()
            .setIssuer("it-shop")
            .setId(user.getId())
            .setSubject(user.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(exp)
            .claim("roles", user.getRoles())
            .signWith(secret, SignatureAlgorithm.HS256)
            .compact();
  }

  Claims getClaimsFromToken(String token){
    return Jwts
            .parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
  }

  String generatePasswordResetToken(ItsUser user, UUID uuid){
    Date exp = calculateExpirationDate(10, ChronoUnit.MINUTES);
    return Jwts.builder()
            .setSubject(user.getId())
            .setExpiration(exp)
            .claim("uuid", uuid.toString())
            .signWith(secret, SignatureAlgorithm.HS256)
            .compact();
  }

  private Date calculateExpirationDate(int amount, ChronoUnit unit){
    ZonedDateTime zonedDateTime = ZonedDateTime.now().plus(amount,unit);
    return Date.from(zonedDateTime.toInstant());
  }

  @PostConstruct
  private void genSecretKey(){
    secret = Keys.hmacShaKeyFor("4M10Bw99WprKH3pLuusCi08jm1FsOu6w".getBytes());
  }
}
