package io.justina.management.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.justina.management.exception.BadRequestException;
import io.justina.management.model.MedicalStaff;
import io.justina.management.model.Patient;
import io.justina.management.model.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;


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
     * @param entity El usuario para el cual se genera el token
     * @return El token JWT generado
     * @throws IllegalArgumentException Si el usuario es nulo
     * @throws RuntimeException Si ocurre un error al crear el token
     */
    public String generateToken(Object entity) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            JWTCreator.Builder jwtBuilder = JWT.create().withIssuer("justina.io");
            String token;
            switch (entity) {
                case User user -> {
                    System.out.println("Generating token for User");
                    token = jwtBuilder
                            .withSubject(user.getEmail())
                            .withClaim("id", user.getPrimaryKey())
                            .withClaim("role", user.getRoleEnum().name())
                            .withClaim("authorities", getRoles(user))
                            .withExpiresAt(generateExpirationDate())
                            .sign(algorithm);
                }
                case Patient patient -> {
                    System.out.println("Generating token for Patient");
                    token = jwtBuilder
                            .withSubject(patient.getEmail())
                            .withClaim("id", patient.getPrimaryKey())
                            .withClaim("role", patient.getRoleEnum().name())
                            .withClaim("authorities", getRoles(patient))
                            .withExpiresAt(generateExpirationDate())
                            .sign(algorithm);
                }
                case MedicalStaff medicalStaff -> {
                    System.out.println("Generating token for MedicalStaff");
                    token = jwtBuilder
                            .withSubject(medicalStaff.getEmail())
                            .withClaim("id", medicalStaff.getPrimaryKey())
                            .withClaim("role", medicalStaff.getRoleEnum().name())
                            .withClaim("authorities", getRoles(medicalStaff))
                            .withExpiresAt(generateExpirationDate())
                            .sign(algorithm);
                }
                case null, default -> throw new IllegalArgumentException("Entity type not supported to generate token.");
            }
            return token;
        } catch (JWTCreationException exception) {
            throw new BadRequestException("Fallo la creación del token.");
        }
    }
//    public String generateToken(Object entity){
//        try{
//            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
//            JWTCreator.Builder jwtBuilder = JWT.create().withIssuer("justina.io");
//            String token;
//            return switch (entity) {
//                case User user -> {
//                    token = jwtBuilder
//                        .withSubject(user.getEmail())
//                        .withClaim("id", user.getId())
//                        .withClaim("role", user.getRoleEnum().name())
//                        .withClaim("authorities", getRoles(user))
//                        .withExpiresAt(generateExpirationDate())
//                        .sign(algorithm);}
//                case Patient patient -> jwtBuilder
//                        .withSubject(patient.getEmail())
//                        .withClaim("id", patient.getId())
//                        .withClaim("role", patient.getRoleEnum().name())
//                        .withClaim("authorities", getRoles(patient))
//                        .withExpiresAt(generateExpirationDate())
//                        .sign(algorithm);
//                case MedicalStaff medicalStaff -> jwtBuilder
//                        .withSubject(medicalStaff.getEmail())
//                        .withClaim("id", medicalStaff.getId())
//                        .withClaim("role", medicalStaff.getRoleEnum().name())
//                        .withClaim("authorities", getRoles(medicalStaff))
//                        .withExpiresAt(generateExpirationDate())
//                        .sign(algorithm);
//                case null, default ->
//                        throw new IllegalArgumentException("Entity type not supported to generate token.");
//            };
//        }catch (JWTCreationException exception){
//            throw new RuntimeException("Failed to create token.");
//        }
//    }
//    public String generateToken(User user){
//        if(user == null){
//            throw new IllegalArgumentException("User cannot be null");
//        }
//        try{
//            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
//            JWTCreator.Builder jwtBuilder = JWT.create().withIssuer("justina.io");
//            return jwtBuilder
//                    .withSubject(user.getEmail())
//                    .withClaim("id", user.getId())
//                    .withClaim("role", user.getRoleEnum().name())
//                    .withClaim("authorities", getRoles(user))
//                    .withExpiresAt(generateExpirationDate())
//                    .sign(algorithm);
//
//        }catch (JWTCreationException exception){
//            throw new RuntimeException("Failed to create token.");
//        }
//    }

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

    /**
     * Obtiene los roles del usuario a partir de las authorities.
     *
     * @param entity El usuario del cual se obtienen los roles
     * @return Lista de roles del usuario
     */
    private List<String> getRoles(Object entity){
        return switch (entity) {
            case User user -> user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            case Patient patient -> patient.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            case MedicalStaff medicalStaff -> medicalStaff.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            case null, default -> throw new BadRequestException("User type not supported to get roles.");
        };
    }

    /**
     * Verifica si el token JWT contiene el rol especificado.
     *
     * @param token El token JWT que se desea verificar
     * @param role El rol que se desea comprobar
     * @return true si el token contiene el rol especificado, false de lo contrario
     * @throws RuntimeException Sí ocurre un error al decodificar el token
     */
    public boolean hasRol(String token, String role){
        if(token == null || token.isBlank()){
            throw new RuntimeException("Token cannot be null or empty.");
        }
        try{
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            DecodedJWT decodedJWT = JWT.decode(token);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("justina.io")
                    .build();
            verifier.verify(token);
            List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
            return authorities.contains(role);
        }catch (JWTDecodeException exception){
            return false;
        }
    }

    /**
     * Genera la fecha de expiración del token JWT.
     *
     * @return La fecha de expiración del token JWT (1 hora desde ahora)
     */
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(72).toInstant(ZoneOffset.of("-05:00"));
    }
}