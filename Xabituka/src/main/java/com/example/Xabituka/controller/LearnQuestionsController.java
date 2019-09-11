package com.example.Xabituka.controller;

import com.example.Xabituka.model.LearnQuestions;
import com.example.Xabituka.repository.LearnQuestionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/learnquestions"})
public class LearnQuestionsController {

    private LearnQuestionsRepository repository;

    public LearnQuestionsController(LearnQuestionsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List findAll() {
        return repository.findAll();
    }

    @GetMapping({"/{id}"})
    public ResponseEntity findById(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }
 
    @PostMapping
    public LearnQuestions create(@RequestBody LearnQuestions learnQuestion){
        return repository.save(learnQuestion);
    }
}
