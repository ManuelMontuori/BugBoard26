package com.bugboard.api.services;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.dto.UserWorkloadOutDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDTO create(UserDTO dto);

    List<UserDTO> findAll();

    Optional<UserDTO> findByUuid(UUID uuid);
   
    void disableUser(UUID uuid);

    void enableUser(UUID uuid);

    List<UserDTO> findAllDisabledUsers();

    List<UserWorkloadOutDTO> findByWorkload();

    List<UserReportDTO> getMonthlyReport(int year, int month);


}
