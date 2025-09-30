package rmsse.infosecurity1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rmsse.infosecurity1.dto.JwtResponse;
import rmsse.infosecurity1.dto.LoginRequest;
import rmsse.infosecurity1.entities.User;
import rmsse.infosecurity1.repositories.UserRepository;
import rmsse.infosecurity1.utils.JwtUtils;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        return new JwtResponse(jwt, loginRequest.getUsername());
    }

    public boolean registerUser(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }

        if (userRepository.existsByEmail(email)) {
            return false;
        }

        // Хеширование пароля перед сохранением
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(username, email, encodedPassword);
        userRepository.save(user);

        return true;
    }
}