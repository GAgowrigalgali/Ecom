package org.example.ecom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.ecom.entity.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "This-is-my-secret-key-should-not-be-disclosed";
    private static final long EXPIRATION_TIME = 60*60*1000;
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())   //subject is identity this token represents,in this case user, hence email is used as it is unique
                .claim("userId", user.getUserId()) //claim is a key value pair inside the payload
                .claim("role", user.getRole().name()) //filter out all the endpoints at the start
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //validity of the token
                .signWith(key, SignatureAlgorithm.HS256) //signed with my key and HS256 algorithm
                .compact(); //serialisers the header and the payload and returns a string after appending the signature
//construction of JWT step by step, it is a builder pattern
    }
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()//read and verify a JWT
                    .setSigningKey(key) //this token is used to verify the signature
                    .build()
                    .parseClaimsJws(token);//Verifies signature,token structure,Checks expiration (exp),Checks issued-at validity ,Decodes header + payload
            return true;

        }catch(JwtException | IllegalArgumentException e ){
            return false;
        }
    }
    //The JWT token is trusted and now the user information is checked
    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();// verifies the token again and gets the body and is returned as Claims, it is Map<Stirng,object>
    }

}
