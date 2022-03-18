package com.company.util;

import com.company.dto.ProfileJwtDTO;
import com.company.enums.ProfileRole;
import com.company.exception.ForbiddenException;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {
    private final static  String secretKey = "secretKey";
    public static String createJwt (Integer id, ProfileRole profileRole) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id));
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));
        jwtBuilder.setIssuer("kun.uz");
        jwtBuilder.claim("role", profileRole.name());
        String jwt = jwtBuilder.compact();
        return jwt;
    }
    public static String createJwt (Integer id) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id));
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));
        jwtBuilder.setIssuer("kun.uz");
        String jwt = jwtBuilder.compact();
        return jwt;
    }

    public static ProfileJwtDTO decodeJwt (String jwt) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);
        Jws jws = jwtParser.parseClaimsJws(jwt);
        Claims claims = (Claims)  jws.getBody();
        String id = claims.getSubject();
        ProfileRole profileRole = ProfileRole.valueOf((String) claims.get("role"));
        return new ProfileJwtDTO(Integer.parseInt(id), profileRole);
     }

    public static String AuthorizationDecodeJwt (String jwt) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);
        Jws jws = jwtParser.parseClaimsJws(jwt);
        Claims claims = (Claims)  jws.getBody();
        String id = claims.getSubject();
        return id;
    }
     public static ProfileJwtDTO getProfile (HttpServletRequest request, ProfileRole role) {
         ProfileJwtDTO jwtDTO = (ProfileJwtDTO) request.getAttribute("jwtDTO");
         if (jwtDTO == null) {
             throw new ForbiddenException("Your jwt Null");
         }
         if (!role.equals(jwtDTO.getProfileRole())) {
             throw new ForbiddenException("You are not allowed to use this feature");
         }
         return jwtDTO;
     }
    public static ProfileJwtDTO getProfile (HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = (ProfileJwtDTO) request.getAttribute("jwtDTO");
        if (jwtDTO == null) {
            throw new ForbiddenException("Your jwt Null");
        }
        return jwtDTO;
    }

}
