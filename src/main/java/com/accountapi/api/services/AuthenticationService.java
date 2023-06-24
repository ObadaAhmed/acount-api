package com.accountapi.api.services;

import com.accountapi.api.models.User;
import com.accountapi.api.models.request.AuthRequest;
import com.accountapi.api.models.response.AuthResponse;
import com.accountapi.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    public AuthResponse authenticate(AuthRequest authRequest) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
           var user = userRepository.findByUsername(authRequest.getUsername());
           String token = jwtService.generateToken(user);
           return AuthResponse.builder()
                   .access_token(token)
                   .build();
    }

    public User getUserDetails(String username) {
        return userRepository.findByUsername(username);
    }
}
