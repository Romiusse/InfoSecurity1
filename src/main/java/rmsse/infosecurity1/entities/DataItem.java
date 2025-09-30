package rmsse.infosecurity1.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_items")
public class DataItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Конструкторы
    public DataItem() {}

    public DataItem(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        // ФИКС: Создаем новый User только с ID вместо принятия внешнего мутабельного объекта
        this.user = new User();
        this.user.setId(userId);
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content != null ?
                content.replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#x27;")
                        .replace("/", "&#x2F;") : null;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public UserDTO getUser() {
        if (this.user == null) {
            return null;
        }
        return new UserDTO(this.user.getId(), this.user.getUsername());
    }

    // ФИКС: Принимаем только ID пользователя вместо целого объекта
    public void setUserId(Long userId) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setId(userId);
    }

    protected User getUserEntity() {
        return this.user;
    }

    public static class UserDTO {
        private final Long id;
        private final String username;

        public UserDTO(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public Long getId() { return id; }
        public String getUsername() { return username; }
    }
}