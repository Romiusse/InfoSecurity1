package rmsse.infosecurity1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rmsse.infosecurity1.entities.User;
import rmsse.infosecurity1.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<Map<String, Object>> userResponses = users.stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("username", user.getUsername());
                    userMap.put("email", user.getEmail());
                    userMap.put("createdAt", user.getCreatedAt());
                    // Пароль НЕ включается в ответ - защита от утечки
                    return userMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> getProfile() {
        Map<String, String> profile = new HashMap<>();
        profile.put("message", "This is your profile page");
        profile.put("username", "current_user"); // В реальном приложении из SecurityContext
        profile.put("role", "ROLE_USER");
        return ResponseEntity.ok(profile);
    }
}