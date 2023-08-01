package com.example.SpringSecurityJWT.api.controller;

import com.example.SpringSecurityJWT.api.models.request.UserRequest;
import com.example.SpringSecurityJWT.api.models.response.UserResponse;
import com.example.SpringSecurityJWT.infrastructure.abstract_services.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final IUserService iUserService;

    @GetMapping(path = "hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello world Not Security");
    }

    @GetMapping(path = "helloSecurity")
    public ResponseEntity<String> helloSecurity(){
        return ResponseEntity.ok("Hello world Security");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")     podemos asignale varios roles al servicio forma1
    //@PreAuthorize("hasAnyRole('USER','ADMIN')")              podemos asignale varios roles al servicio forma2
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ){
        log.info("request user: {} " , request);
        return ResponseEntity.ok(iUserService.create(request));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id){
        iUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
