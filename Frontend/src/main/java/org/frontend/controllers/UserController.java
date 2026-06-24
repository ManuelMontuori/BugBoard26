package org.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.frontend.models.User;
import org.frontend.services.UserService;
import org.frontend.util.BackendServiceFactory;

public class UserController {

    private final UserService service;
    private final ObservableList<User> users;

    public UserController() {
        this.service = BackendServiceFactory.getInstance().getUserService();
        this.users   = FXCollections.observableArrayList();
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void loadAllUsers() {
        users.setAll(service.findAll());
    }

    public void createUser(String email, String role) {
        User user = new User();
        user.setEmail(email);
        user.setRole(role);

        service.createUser(user);
    }
}