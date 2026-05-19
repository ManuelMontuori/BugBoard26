package com.bugboard.api.services;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.dto.UserWorkloadOutDTO;
import com.bugboard.api.dto.WorkloadDTO;
import com.bugboard.api.mapper.UserMapper;
import com.bugboard.api.models.User;
import com.bugboard.api.models.UserStatus;
import com.bugboard.api.repositories.UserRepositoryAdaptee;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    // la dependency injection sostituisce l'Autowired. Il been container gentisce automaticamentel'inejction
    private final UserServiceTarget userServiceTarget;
    private final UserMapper userMapper;
    public UserServiceImpl(UserServiceTarget userServiceTarget, UserMapper userMapper) {
        this.userServiceTarget = userServiceTarget;
        this.userMapper=userMapper;
    }


    @Override
    public UserDTO create(UserDTO dto) {
        User user = new User();
        userMapper.mapToEntity(dto, user);
        User saved = userServiceTarget.save(user);
        return userMapper.mapToDTO(saved);
    }

    @Override
    public List<UserDTO> findAll() {
        return userServiceTarget.findAll().stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    @Override
    public void disableUser(UUID uuid) {
        User user = userServiceTarget.findByUuid(uuid).orElseThrow(() -> new IllegalStateException("User not found"));
        user.disable();
    }

    @Override
    public void enableUser(UUID uuid) {
        User user = userServiceTarget.findByUuid(uuid).orElseThrow(() -> new IllegalStateException("User not found"));
        user.enable();
    }

    @Override
    public List<UserDTO> findAllDisabledUsers() {
        return userServiceTarget.findAllByStatus(UserStatus.DISABLED).stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    @Override
    public List<UserWorkloadOutDTO> findByWorkload() {
        return userServiceTarget.findByWorkload()
                .stream()
                .map(userMapper::mapWorkloadToWorkloadOut)
                .toList();
    }

    @Override
    public Optional<UserDTO> findByUuid(UUID uuid) {
//        UUID userUuid = UUID.fromString(uuid); // converto la stringa in UUID
        return userServiceTarget.findByUuidAndStatus(uuid, UserStatus.ACTIVE).map(userMapper::mapToDTO);
    }

    @Override
    public UserDTO update(String uuid, UserDTO dto) {
        return null;
    }

    @Override
    public void delete(String uuid) {

    }



//    @Autowired
//    private UserRepository userRepository;
//
//    public List<UserDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        List<UserDTO> result = new ArrayList<>();
//        for(User user : users) {
//            UserDTO dto = toDTO(user);
//            result.add(dto);
//        }
//        return result;
//    }
//
//    private UserDTO toDTO(User user) {
//        return new UserDTO(
//                user.getId(),
//                user.getEmail(),
//                user.getRole().name()
//        );
//    }
//
//    public Optional<UserDTO> getUserById(Long id) { // dava troppi problemi con Optional<UserDTO> e non capivo perché,
//        // alla fine ho deciso di restituire un Optional<User> e poi mappare a DTO, ma bisogna capire come
//        // funziona la map
//        return userRepository.findById(id)
//                .map(this::toDTO);
//
//    }
}
