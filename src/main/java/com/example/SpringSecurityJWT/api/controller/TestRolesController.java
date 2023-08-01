package com.example.SpringSecurityJWT.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "test-role")
public class TestRolesController {

    @GetMapping(path = "/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> accessAdmin(){
      return ResponseEntity.ok("Hola, has accedido con rol de ADMIN");
    }

    @GetMapping(path = "/accessUser")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")     podemos asignale varios roles al servicio forma1
    //@PreAuthorize("hasAnyRole('USER','ADMIN')")              podemos asignale varios roles al servicio forma2
    @PreAuthorize("hasRole('USER')") //ASIGANMOS EL ROL QUE VA PERMITIR EJECUTAR EL SERVICIO
    public  ResponseEntity<String> accessUser(){
        return ResponseEntity.ok("Hola, has accedido con rol de USER");
    }

    @GetMapping(path = "/accessInvited")
    @PreAuthorize("hasRole('INVITED')")
    public  ResponseEntity<String> accessInvited(){
        return ResponseEntity.ok("Hola, has accedido con rol de INVITED");
    }
}
