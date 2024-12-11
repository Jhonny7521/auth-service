package com.bm_ntt_data.auth_service.security;

import com.bm_ntt_data.auth_service.entity.AuthUser;
import com.bm_ntt_data.auth_service.model.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        //secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

    public TokenDto createToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        //claims = Jwts.claims().setSubject(authUser.getUserName());
        claims.put("sub", authUser.getUserName());
        claims.put("id", authUser.getId());
        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600000);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(
                Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
//                .signWith(SignatureAlgorithm.HS256, secret)
                .signWith(key)
                .compact());
        return tokenDto;
    }

    public boolean validate(TokenDto token) {
        try {
            Jwts
                    .parser()
//                    .setSigningKey(secret)
                    .setSigningKey(key)
                    .parseClaimsJws(token.getToken());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUserNameFromToken(String token){
        try {
            return Jwts
                    .parser()
//                    .setSigningKey(secret)
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (Exception e) {
            return "bad token";
        }
    }

}
