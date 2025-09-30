package rmsse.infosecurity1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmsse.infosecurity1.dto.DataItemResponse;
import rmsse.infosecurity1.entities.DataItem;
import rmsse.infosecurity1.entities.User;
import rmsse.infosecurity1.repositories.DataItemRepository;
import rmsse.infosecurity1.repositories.UserRepository;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired
    private DataItemRepository dataItemRepository;

    @Autowired
    private UserRepository userRepository;

    public List<DataItemResponse> getAllData() {
        return dataItemRepository.findAllWithUser().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DataItemResponse> getUserData(String username) {
        return dataItemRepository.findByUsername(username).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DataItemResponse createDataItem(String title, String content, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Экранирование с помощью Spring
        String escapedTitle = HtmlUtils.htmlEscape(title);
        String escapedContent = HtmlUtils.htmlEscape(content);

        DataItem dataItem = new DataItem(escapedTitle, escapedContent, user.getId());
        DataItem savedItem = dataItemRepository.save(dataItem);

        return convertToResponse(savedItem);
    }

    private DataItemResponse convertToResponse(DataItem dataItem) {
        DataItem.UserDTO userDTO = dataItem.getUser();
        String author = userDTO != null ? userDTO.getUsername() : "Unknown";

        return new DataItemResponse(
                dataItem.getId(),
                dataItem.getTitle(),
                dataItem.getContent(),
                dataItem.getCreatedAt(),
                author
        );
    }
}