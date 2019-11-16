package com.example.Xabituka.controller;

import com.example.Xabituka.model.Users;
import com.example.Xabituka.repository.UsersRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public Users create(@RequestBody Users user) {
        return repository.save(user);
    }

    @GetMapping({"/login"})
    public LinkedHashMap teste(@RequestParam String nickname, @RequestParam String password) {
        Map res = new LinkedHashMap();
        try {
            Users user = repository.findByNickname(nickname);

            if (user.getPw().equals(password)) {
                res.put("auth", true);
                res.put("userType", user.getUserType());
                res.put("userId", user.getId());
            }
            else {
                res.put("auth", false);
            }
        }
        catch(Exception e) {
            res.put("auth", false);
        }

        return (LinkedHashMap) res;
    }
}
