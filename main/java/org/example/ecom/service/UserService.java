package org.example.ecom.service;

import org.example.ecom.dtos.request.LoginRequest;
import org.example.ecom.dtos.request.UserRegisterRequest;
import org.example.ecom.dtos.response.LoginResponse;
import org.example.ecom.dtos.response.UserResponse;
import org.example.ecom.entity.Role;
import org.example.ecom.entity.User;
import org.example.ecom.repository.UserRepository;
import org.example.ecom.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    public UserResponse register(UserRegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email address already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.CUSTOMER);

        //save user
        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        return response;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        boolean matchPasswords = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        );
        if (!matchPasswords) {
            throw new RuntimeException("Invalid email or password");
        }
        String  token = jwtUtil.generateToken(user); //injected JwtUtil via constructor.

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setToken(token);

        return response;

    }


}
