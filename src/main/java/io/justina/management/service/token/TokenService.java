package io.justina.management.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.justina.management.model.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;


/**
 * Servicio para la gestión de tokens JWT.
 * Implementa la interfaz ITokenService.
 */
@Getter
@Service
public class TokenService implements ITokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    /**
     * Genera un token JWT para el usuario proporcionado.
     *
     * @param user El usuario para el cual se genera el token
     * @return El token JWT generado
     * @throws IllegalArgumentException Si el usuario es nulo
     * @throws RuntimeException Si ocurre un error al crear el token
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("justina.io")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRoleEnum().name())
                    .withClaim("nombre", user.getFirstName())
                    .withExpiresAt(Date.from(generateExpirationDate()))
                    .sign(algorithm);
        }
        catch (JWTCreationException exception){
            throw new RuntimeException("Failed to create token.");
        }
    }
    /**
     * Obtiene el sujeto (subject) del token JWT proporcionado.
     *
     * @param token El token JWT del cual se desea obtener el sujeto
     * @return El sujeto (subject) del token JWT
     * @throws IllegalArgumentException Si el token es nulo o vacío
     * @throws RuntimeException Si ocurre un error al verificar el token
     */
    public String getSubject(String token){
        if(token == null || token.isBlank()){
            throw new IllegalArgumentException("Token cannot be null or empty.");
        }
        DecodedJWT verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("justina.io")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTCreationException exception){
            throw new RuntimeException("Failed to verify token.");
        }
        if(verifier.getSubject() == null){
            throw new RuntimeException("Verifier invalid");
        }
        return verifier.getSubject();
    }


    public String getUsernameFromToken(String token) {
        if(token == null){
            throw new RuntimeException("Token nulo");
        }
        DecodedJWT verifier;
        try{
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("justina.io")
                    .build()
                    .verify(token);
            verifier.getSubject();
        }catch (JWTCreationException e){
            throw new RuntimeException("Error al verificar el token");
        }
        if (verifier.getSubject() == null){
            throw new RuntimeException("Verificador invalido");
        }
        return verifier.getSubject();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    private Date getExpirationDateFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }
    public String getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("role").asString();
        } catch (JWTDecodeException exception){
            throw new RuntimeException("Invalid token");
        }
    }

    /**
     * Genera la fecha de expiración del token JWT.
     *
     * @return La fecha de expiración del token JWT (1 hora desde ahora)
     */
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(168).toInstant(ZoneOffset.of("-05:00"));
    }
}