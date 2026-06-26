package org.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.frontend.models.User;
import org.frontend.models.UserWorkload;
import org.frontend.models.dtos.UserReportDTO;
import org.frontend.services.UserService;
import org.frontend.util.BackendServiceFactory;

import java.util.List;

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

    public void createUser(String email, String role, String firstName, String lastName) {
        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        service.createUser(user);
    }

    private final ObservableList<UserWorkload> workload =
            FXCollections.observableArrayList();

    public ObservableList<UserWorkload> getWorkload() { return workload; }

    public void loadWorkload() {
        workload.setAll(service.findByWorkload());
    }

    public void assignIssue(String issueUuid, String userUuid) {
        service.assignIssue(issueUuid, userUuid);
    }

    // Restituisce il primo utente (carico minore — già ordinato dal backend)
    public UserWorkload getSuggerito() {
        return workload.isEmpty() ? null : workload.get(0);
    }

    public List<UserReportDTO> getMonthlyReport(int year, int month) {
        return service.getMonthlyReport(year, month);
    }

    // Esempio logico di metodi nell'UserController:
    public void enableUser(String uuid) throws Exception {
        service.enableUser(uuid);
        loadAllUsers(); // Ricarica la lista per sincronizzare la UI
    }

    public void disableUser(String uuid) throws Exception {
        service.disableUser(uuid);
        loadAllUsers();
    }
}