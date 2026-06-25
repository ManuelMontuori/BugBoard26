package org.frontend.models;

import javafx.beans.property.*;
import org.frontend.models.dtos.UserDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

    private final StringProperty firstName =
            new SimpleStringProperty();

    private final StringProperty lastName =
            new SimpleStringProperty();

    private final StringProperty uuid =
            new SimpleStringProperty();

    private final StringProperty email =
            new SimpleStringProperty();

    private final StringProperty role =
            new SimpleStringProperty();

    private final StringProperty status =
            new SimpleStringProperty();

    private final ObjectProperty<LocalDate> lastLogin =
            new SimpleObjectProperty<>();

    private final ObjectProperty<LocalDateTime> createdAt =
            new SimpleObjectProperty<>();

    public User() {}

    public User(UserDTO dto) {
        uuid.set(dto.uuid());
        email.set(dto.email());
        role.set(dto.role());
        firstName.set(dto.firstName());
        lastName.set(dto.lastName());
        // status, lastLogin, createdAt non sono nel DTO ora —
        // verranno aggiunti al DTO quando il backend li esporrà
    }

    // --- uuid ---
    public String getUuid()             { return uuid.get(); }
    public StringProperty uuidProperty(){ return uuid; }

    // --- email ---
    public String getEmail()              { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public void setEmail(String email)    { this.email.set(email); }

    // --- role ---
    public String getRole()              { return role.get(); }
    public StringProperty roleProperty() { return role; }
    public void setRole(String role)     { this.role.set(role); }

    // --- status ---
    public String getStatus()              { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String status)   { this.status.set(status); }

    // --- lastLogin ---
    public LocalDate getLastLogin()                      { return lastLogin.get(); }
    public ObjectProperty<LocalDate> lastLoginProperty() { return lastLogin; }
    public void setLastLogin(LocalDate lastLogin)        { this.lastLogin.set(lastLogin); }

    // --- createdAt ---
    public LocalDateTime getCreatedAt()                       { return createdAt.get(); }
    public ObjectProperty<LocalDateTime> createdAtProperty()  { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)         { this.createdAt.set(createdAt); }

    // firstName
    public String getFirstName()              { return firstName.get(); }
    public StringProperty firstNameProperty()               { return firstName; }
    public void setFirstName(String firstName)        { this.firstName.set(firstName); }

    // lastName
    public String getLastName()                  { return lastName.get(); }
    public StringProperty lastNameProperty()             { return lastName; }
    public void setLastName(String lastName)        { this.lastName.set(lastName); }
}