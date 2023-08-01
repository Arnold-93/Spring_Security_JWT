package com.example.SpringSecurityJWT.config.filter;

import com.example.SpringSecurityJWT.config.jwt.JwUtil;
import com.example.SpringSecurityJWT.infrastructure.servicios.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * OncePerRequestFilter significa que se va a autenticar una ves por peticion es decir cada ves que se ejecute algun servicio se va autenticar
 * */
@Component
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

     private final JwUtil jwUtil; //como vamos a validar el token, vamos a utilizar jwUtil donde se encuentra la peticion para obtener el token

    private final UserDetailsServiceImpl userDetailsServiceImpl; //necesitamos el userDetailsServiceImpl por qué necesitamos los datos de la base de datos

    //los parametros no puden ser nulos nuca es por ese que se va agregar @NotNull
    @Override
    protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {

        //obteniendo el toquen del header
        String tokenHeader = request.getHeader("Authorization");

        //validamos el token
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            String username = jwUtil.getUsernameFromToken(tokenHeader.substring( 7, tokenHeader.length())); //obtenemos el nombre del usuario (lo sacamos de una parte del token los claim (PAYLOAD:DATAs))
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username); //obtenemos el detalle de los usuarios y su respectivo roles de la BD convertido en UserDetails propio de spring security

            //nos autenticamos con UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, //enviamos el usuario
                    null, // a ca enviamos la contraseña, pero spring security lo tomará desde el UserDetails
                    userDetails.getAuthorities() //enviamos los permisos del usuario es decir los roles

            );

            //vamos a setear la utenticacion nueva del usuario especificando los roles del usuario
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        }

        // filterChain.doFilter esto quiere decir que va a continuar con el filtro de validation y se dara cuenta por interno que el token no está autenticado
        filterChain.doFilter(request, response);
    }
}
