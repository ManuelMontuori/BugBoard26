package com.bugboard.api.controllers;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class  UserController {
//    @Autowired
//    private UserServiceImpl userServiceImpl;

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService=userService;
    }

    @GetMapping("/{uuid}")
    public UserDTO findByUuid(@PathVariable UUID uuid) {
        return userService.findByUuid(uuid).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
        // qui non serve il controllo perché se non ci sono utenti, restituisce una lista vuota, che è un risultato valido
    }

    @GetMapping("/disabled")
    public List<UserDTO> findAllDisabledUsers() {
        return userService.findAllDisabledUsers();
    }

    @GetMapping("/workload")
    public List<UserDTO> findByWorkload() {
        return userService.findByWorkload();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserDTO dto) {
        return userService.create(dto);
    }


    @PatchMapping("/disable/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable UUID uuid) {
        userService.disableUser(uuid);
    }

    @PatchMapping("/enable/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable UUID uuid) {
        userService.enableUser(uuid);
    }





}
