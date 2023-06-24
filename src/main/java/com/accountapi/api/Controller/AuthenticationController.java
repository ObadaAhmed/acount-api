package com.accountapi.api.Controller;


import com.accountapi.api.models.User;
import com.accountapi.api.models.request.AuthRequest;
import com.accountapi.api.models.response.AuthResponse;
import com.accountapi.api.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authRequest));
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Perform any additional logout operations if needed

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }
    @GetMapping("/user-details")
    public User fetchUserDetails(@RequestParam String username) {
        return authenticationService.getUserDetails(username);
    }
}
