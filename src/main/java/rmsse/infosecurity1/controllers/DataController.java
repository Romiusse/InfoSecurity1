package rmsse.infosecurity1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rmsse.infosecurity1.dto.DataItemResponse;
import rmsse.infosecurity1.services.DataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DataItemResponse>> getAllData() {
        List<DataItemResponse> data = dataService.getAllData();
        return ResponseEntity.ok(data);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createDataItem(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String title = request.get("title");
        String content = request.get("content");

        if (title == null || content == null || title.trim().isEmpty() || content.trim().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Title and content are required");
            return ResponseEntity.badRequest().body(response);
        }

        String username = authentication.getName();
        DataItemResponse createdItem = dataService.createDataItem(title, content, username);
        return ResponseEntity.ok(createdItem);
    }
}