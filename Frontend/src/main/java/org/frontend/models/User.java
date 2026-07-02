package org.frontend.models;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
        status.set(dto.status());
        lastLogin.set(dto.lastLogin());
        createdAt.set(dto.createdAt());
    }

    public String getUuid()             { return uuid.get(); }

    public void setEmail(String email)    { this.email.set(email); }

    public void setRole(String role)     { this.role.set(role); }

    public String getStatus()              { return status.get(); }
    public StringProperty statusProperty() { return status; }

    public StringProperty firstNameProperty()               { return firstName; }
    public void setFirstName(String firstName)        { this.firstName.set(firstName); }

    public StringProperty lastNameProperty()             { return lastName; }
    public void setLastName(String lastName)        { this.lastName.set(lastName); }

    public BooleanBinding activeProperty() {
        // se statusProperty cambia, il booleano si ricalcola da solo
        return Bindings.createBooleanBinding(
                () -> "ACTIVE".equalsIgnoreCase(getStatus()),
                statusProperty()
        );
    }
}