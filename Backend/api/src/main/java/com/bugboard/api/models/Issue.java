package com.bugboard.api.models;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_type")
    private IssueType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_priority")
    private IssuePriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_status", nullable = false)
    private IssueStatus status=IssueStatus.TODO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(nullable = false, updatable = false, name = "created_at")
        private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "img_path")
    private String imagePath;

    // costruttore vuoto obbligatorio
    public Issue() {}

    // tutti i getter
    public Long getId() {
        return id;
    }

    public UUID getUuid() { return uuid; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public IssueType getType() {
        return type;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public User getReporter() {
        return reporter;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    // setter tutti tranne id

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setUuid(UUID uuid) { this.uuid = uuid; }
}
