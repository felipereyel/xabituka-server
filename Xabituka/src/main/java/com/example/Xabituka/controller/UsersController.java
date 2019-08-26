package com.example.Xabituka.controller;

import com.example.Xabituka.model.Users;
import com.example.Xabituka.repository.UsersRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/users"})
public class UsersController {

    private UsersRepository repository;

    public UsersController(UsersRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List findAll() {
        return repository.findAll();
    }

    @PostMapping
    public Users create(@RequestBody Users users){
        return repository.save(users);
    }
}
