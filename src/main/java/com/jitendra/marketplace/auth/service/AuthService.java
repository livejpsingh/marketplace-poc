package com.jitendra.marketplace.auth.service;

import com.jitendra.marketplace.auth.dto.AuthResponse;
import com.jitendra.marketplace.auth.dto.LoginRequest;
import com.jitendra.marketplace.auth.dto.RegisterRequest;
import com.jitendra.marketplace.auth.dto.UserProfileResponse;
import com.jitendra.marketplace.auth.model.Role;
import com.jitendra.marketplace.auth.model.RoleName;
import com.jitendra.marketplace.auth.model.User;
import com.jitendra.marketplace.auth.repo.RoleRepository;
import com.jitendra.marketplace.auth.repo.UserRepository;
import com.jitendra.marketplace.auth.security.JwtService;
import java.util.List;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        RoleName roleName = mapRole(request.getRole());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not configured: " + roleName));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        userRepository.save(user);

        // Auto-login after successful registration to match frontend auth flow.
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(request.getPassword());
        return login(loginRequest);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        String token = jwtService.generateToken(principal);
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                principal.getUsername(),
                user.getFullName(),
                roles
        );
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();
        return new UserProfileResponse(user.getEmail(), user.getFullName(), roles);
    }

    private RoleName mapRole(String role) {
        return switch (role.toUpperCase()) {
            case "BUYER" -> RoleName.ROLE_BUYER;
            case "SUPPLIER" -> RoleName.ROLE_SUPPLIER;
            default -> throw new IllegalArgumentException("Unsupported role");
        };
    }
}
