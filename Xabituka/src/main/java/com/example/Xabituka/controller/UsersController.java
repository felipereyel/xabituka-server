package com.example.Xabituka.controller;

import com.example.Xabituka.model.Users;
import com.example.Xabituka.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = {"/{id}"})
    public ResponseEntity findById(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Users create(@RequestBody Users users){
        return repository.save(users);
    }
}
