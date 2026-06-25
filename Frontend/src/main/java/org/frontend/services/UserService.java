package org.frontend.services;

import org.frontend.models.User;
import org.frontend.models.dtos.UserDTO;
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

            System.out.println("--- DEBUG RESPONSE USER FIND_ALL ---");
            System.out.println(json);
            System.out.println("------------------------------------");

            List<UserDTO> dtoList = JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory()
                            .constructCollectionType(List.class, UserDTO.class)
            );

            return dtoList.stream()
                    .map(User::new)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public User createUser(User user) {
        try {
            String json = JsonUtil.mapper.writeValueAsString(user);

            System.out.println("--- DEBUG POST USER PAYLOAD ---");
            System.out.println(json);
            System.out.println("-------------------------------");

            String jsonResponse = api.create(json);

            System.out.println("--- DEBUG POST USER RESPONSE ---");
            System.out.println(jsonResponse);
            System.out.println("--------------------------------");

            UserDTO savedDto = JsonUtil.mapper.readValue(jsonResponse, UserDTO.class);
            return new User(savedDto);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}