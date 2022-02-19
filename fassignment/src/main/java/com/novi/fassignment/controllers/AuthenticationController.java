package com.novi.fassignment.controllers;

import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.Authority;
import com.novi.fassignment.models.ERole;
import com.novi.fassignment.models.User;
import com.novi.fassignment.payload.JwtResponse;
import com.novi.fassignment.payload.LoginRequest;
import com.novi.fassignment.services.AuthorityService;
import com.novi.fassignment.services.CustomUserDetailsService;
import com.novi.fassignment.services.UserService;
import com.novi.fassignment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.*;


@RestController
@CrossOrigin(origins ="*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;


    @Autowired
    JwtUtil jwtUtl;

    @GetMapping(value = "/authenticated")//This methode is not used anymore in this app but we keep it for the moment
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    @PostMapping(value = "api/auth/signup")
    public ResponseEntity<Object> signup(@RequestBody User user) {

        String newUsername = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();


        //String initialAdministratorUsername="${app.initialAdministratorUsername}";
        String initialAdministratorUsername="cobergfell";
        String username=user.getUsername();

        for (ERole role : ERole.values()) {
            String strRole=role.name();
            if (strRole.equals("ROLE_USER")){
                authorityService.addAuthority(username, strRole);
            }
            else{
                if (username.equals(initialAdministratorUsername)) {
                    authorityService.addAuthority(username, strRole);
                }
            }

        }
        if(user.getAuthorities().size()==0){new RuntimeException("Error: At least one role was not found.");}



        return ResponseEntity.created(location).build();
    }



    @PostMapping("api/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);

        //String jwt = jwtUtils.generateJwtToken(authentication);
        String jwt = jwtUtl.generateToken(userDetails);

        Optional<User> optionalUser=userService.getUser(username);
        if (optionalUser.isPresent()) {
            String email = optionalUser.get().getEmail();
            Set<Authority> authorities=optionalUser.get().getAuthorities();
            ArrayList<String> authoritiesStr = new ArrayList<String>();
            for (Authority authority : authorities) {
                authoritiesStr.add(authority.getAuthority());
            }
            return ResponseEntity.ok(new JwtResponse(jwt,
                    username,
                    email,
                    authoritiesStr));

        } else {
            throw new RecordNotFoundException("Incorrect username or password");
        }

    }


/*    @PostMapping(value = "api/auth/signin") //this was the authentication up to 25-9-21
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        }
        catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);

        final String jwt = jwtUtl.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }*/

}


