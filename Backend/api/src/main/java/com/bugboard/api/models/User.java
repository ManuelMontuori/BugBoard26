package com.bugboard.api.models;

import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @UuidGenerator è una soluzione hibernate per generare un uuid automaticamente. In questo modo, hibernate genera uuid e poi fa una insert.
    // evitando di far generare uuid al database, che non aggiungerebbe uuid all'oggetto di risposta e quindi non vedremmo uuid nel json
    // Si sarebbe dovuto fare una select dopo l'insert per recuperare uuid, ma con questa soluzione non è necessario
    @UuidGenerator
    @Column(nullable = false, updatable = false, unique=true)
    private UUID uuid;


    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30, name="firstname")
    private String firstName;

    @Column(nullable = false, length = 30, name="lastname")
    private String lastName;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRole role;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;


    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDate lastLogin;

    public User() {
        // in jakarta il costruttore di default viene escluso se si definisce un costruttore personalizzato,
        // quindi è necessario definirlo esplicitamente
    }

    public User(UserRole role, String email, UUID uuid) {
        this.role = role;
        this.email = email;
        this.uuid = uuid;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    // metodi di utilità
    public void disable() {
        if (this.status == UserStatus.DISABLED) {
            throw new IllegalStateException("User is already disabled");
        }
        this.status = UserStatus.DISABLED;
    }

    public void enable() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        this.status = UserStatus.ACTIVE;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }


}
