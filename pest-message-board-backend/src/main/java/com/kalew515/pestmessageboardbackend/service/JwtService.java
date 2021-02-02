package com.kalew515.pestmessageboardbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kalew515.pestmessageboardbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private UserService userService;

    public String signToken (Integer uid) {
        Date expireDate = new Date(System.currentTimeMillis() + 8640000000L);
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        JWTCreator.Builder creator = JWT.create().withClaim("uid", uid).withIssuer("com.kalew515").withExpiresAt(expireDate);
        return creator.sign(algorithm);
    }

    public User verifyToken (String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("com.kalew515").build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (Exception exception) {
            return null;
        }
        Integer uid = jwt.getClaim("uid").asInt();
        Date exprTime = jwt.getExpiresAt();
        Date currTime = new Date();
        User user = userService.getUserById(uid);
        if (user != null && exprTime.after(currTime)) {
            return user;
        } else {
            return null;
        }
    }
}
