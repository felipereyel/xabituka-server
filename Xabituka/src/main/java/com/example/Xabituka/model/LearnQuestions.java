package com.example.Xabituka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor // Cria construtores automaticamente
@NoArgsConstructor
@Entity
@Data
public class LearnQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private Boolean answer;
    private Long difficulty;
    @Column(name = "topic_id")
    private Long topicId;
}
