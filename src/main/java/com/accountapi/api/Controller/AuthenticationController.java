package com.accountapi.api.Controller;


import com.accountapi.api.models.User;
import com.accountapi.api.models.request.AuthRequest;
import com.accountapi.api.models.request.RegisterRequest;
import com.accountapi.api.models.response.AuthResponse;
import com.accountapi.api.services.AuthenticationService;
import com.accountapi.api.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(authRequest , authRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        User user = (User) authentication.getPrincipal();
//        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(authenticationService.authenticate(authRequest));
    }

    @GetMapping("/user-details")
    public User fetchUserDetails(@RequestParam String username) {
        return authenticationService.getUserDetails(username);
    }
}
