package com.example.SpringSecurityJWT.config.filter;

import com.example.SpringSecurityJWT.config.jwt.JwUtil;
import com.example.SpringSecurityJWT.domain.entities.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Slf4j
//UsernamePasswordAuthenticationFilter nos va ayudar autenticar en nuestra app
public class JwtAuthenticationFIlter extends UsernamePasswordAuthenticationFilter {

    private final JwUtil jwUtil;

    /**
     * ======================================================================================
     * Click derecho - generate - Override Methods - seleccionamos (attemptAuthentication, successfulAuthentication)
     * ======================================================================================
     * */

    /**
     *Esto nos sirve para validar cuando se intentan autenticar
     * */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        UserEntity userEntity = null;
        String username = "";
        String password = "";

        try {
            log.info(">>>>>>>>> transformamos la data recibida para obtener usuario, constraseña");
            //transformamos la data recibida con la libreria de spring JatSOn
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class); //como request es un objeto utilizamos el ObjectMapper() para rellenar la clase UserEntity
            username =  userEntity.getUsername();
            password = userEntity.getPassword();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info(">>>>>>>>> nos autenticamos");
        // nos autenticamos
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password
        );

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     *Esto nos sirve para decirle a la app si ya esta autenticado correctamente que es lo que vamos hacer
     * */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        log.info(">>>>>>>>> Obtenemos las caracteristicas del usuario");
        User user = (User) authResult.getPrincipal();

        log.info(">>>>>>>>> generamos el token");
        String token = jwUtil.generateAccesToken(user.getUsername());

        //devolvemos el token en el header del servicio
        response.addHeader("Authorization", token);

        //Construimos objeto para luego devolver en la respeusta del servicio
        Map<String, Object> httpResponse = new HashMap<>();
        httpResponse.put("token",token);
        httpResponse.put("Message", "Authorization Correcta");
        httpResponse.put("Username", user.getUsername());

        //devolvemos la data previamente construida en un objeto en el body
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));

        //Agregamos parametros a la repuesta (status: 200, type: application/json )
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush(); // con esto aseguramos que todo se escriba correctamente

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
