package com.example.SpringSecurityJWT.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwUtil {

    @Value("${jwt.secret.key")
    private String secretKey; //firma
    @Value("${jwt.time.expiration")
    private String timeExpiration; //tiempo de expiracion

    /**
     * Generamos token de acceso
     **/
    public String generateAccesToken(String username) {
        log.info(">>>>>>>>> generamos token a partir del usuario: ".concat(username));
        return Jwts.builder()
                .setSubject(username) //indicamos el nombre del usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) //indicamos la fecha actual en milisegundos
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration))) // Indicamos la fecha de expiracion (fecha actual + fechade timeExpiration)
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256) // Obtenemos la fima "getSignatureKey()" y luego utilizamos un algoritmo para encriptar nuevamente la fima "SignatureAlgorithm.HS256"
                .compact();
    }

    /**
     * Validar el token de acceso - verificamos si el token cumple con la firma
     **/
    public boolean isTokenValid(String token) {
        log.info(">>>>>>>>> validamos el siguiente token: ".concat(token));
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey()) //obtenemos la firma
                    .build()
                    .parseClaimsJws(token) //insertamos el token
                    .getBody();
            return true;
        }catch (Exception e){
            log.error("Se encontro un error en el token ingresado: ".concat(e.getMessage()));
            return false;
        }
    }

    /**
     * Obtenemos fima del token
     **/
    public Key getSignatureKey() {
        log.info(">>>>>>>>> Obtebemos firma del token");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //vamos a decodificar el secretKey
        return Keys.hmacShaKeyFor(keyBytes); //devolvemos la firma encriptada
    }


    /**
     * ========================================
     * OBTENEMOS CARACTERISTICAS DEL TOKEN CLAIMS(PAYLOAD:DATA)
     * =========================================
     **/

    /**
     * Obtenemos el usuario que genero el token
     **/
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     *Obtener un solo claim
     **/
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extracAllClaims(token);
        return claimsTFunction.apply(claims);
    }


    /**
     * Obtenemos todos los claims del token
     **/
    public Claims extracAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

}
