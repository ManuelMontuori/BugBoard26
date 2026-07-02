package org.frontend.services;

import org.frontend.models.User;
import org.frontend.models.UserWorkload;
import org.frontend.models.dtos.UserDTO;
import org.frontend.models.dtos.UserReportDTO;
import org.frontend.models.dtos.UserWorkloadDTO;
import org.frontend.util.JsonUtil;
import java.util.List;

public class UserService {

    private final UserApiService api;

    public UserService(UserApiService api) {
        this.api = api;
    }

    public List<User> findAll() {
        try {
            String json = api.findAll();

            List<UserDTO> dtoList = JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory()
                            .constructCollectionType(List.class, UserDTO.class));

            return dtoList.stream()
                    .map(User::new)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public User createUser(User user) throws Exception {

        String json = JsonUtil.mapper.writeValueAsString(user);

        String jsonResponse = api.create(json);

        UserDTO savedDto = JsonUtil.mapper.readValue(jsonResponse, UserDTO.class);
        return new User(savedDto);

    }

    public List<UserWorkload> findByWorkload() {
        try {
            String json = api.findByWorkload();

            List<UserWorkloadDTO> dtoList = JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory()
                            .constructCollectionType(List.class, UserWorkloadDTO.class));

            return dtoList.stream()
                    .map(UserWorkload::new)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void assignIssue(String issueUuid, String userUuid) {
        try {
            api.assignIssue(issueUuid, userUuid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<UserReportDTO> getMonthlyReport(int year, int month) {
        try {
            String json = api.getMonthlyReport(year, month);

            return JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory()
                            .constructCollectionType(List.class, UserReportDTO.class));

        } catch (Exception e) {
            System.err.println("Errore durante il recupero del report mensile: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Ritorna una lista vuota in caso di errore per evitare NullPointerException
        }
    }

    public void enableUser(String uuid) throws Exception {
        try {
            api.enable(uuid);
        } catch (Exception e) {
            System.err.println("UserService: Errore durante l'abilitazione dell'utente " + uuid);
            e.printStackTrace();
            throw e; // Rilanciamo l'eccezione per farla gestire al ViewController grafico
        }
    }

    public void disableUser(String uuid) throws Exception {
        try {
            api.disable(uuid);
        } catch (Exception e) {
            System.err.println("UserService: Errore durante la disabilitazione dell'utente " + uuid);
            e.printStackTrace();
            throw e;
        }
    }
}