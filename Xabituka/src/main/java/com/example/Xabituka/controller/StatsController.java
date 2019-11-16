package com.example.Xabituka.controller;

import com.example.Xabituka.model.LearnAnswers;
import com.example.Xabituka.model.LearnQuestions;
import com.example.Xabituka.model.Subjects;
import com.example.Xabituka.model.Topics;
import com.example.Xabituka.repository.LearnAnswersRepository;
import com.example.Xabituka.repository.LearnQuestionsRepository;
import com.example.Xabituka.repository.SubjectsRepository;
import com.example.Xabituka.repository.TopicsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.Xabituka.model.LearnQuestions.JoinQuestions;
import static com.example.Xabituka.model.LearnQuestions.applyHeuristic;

@RequestMapping({"/stats"})
@RestController
public class StatsController {
    private LearnQuestionsRepository learnQuestionsRepository;
    private TopicsRepository topicsRepository;
    private LearnAnswersRepository learnAnswersRepository;
    private SubjectsRepository subjectsRepository;

    public StatsController(
            LearnQuestionsRepository learnQuestionsRepository,
            TopicsRepository topicsRepository,
            LearnAnswersRepository learnAnswersRepository,
            SubjectsRepository subjectsRepository
    ) {
        this.learnQuestionsRepository = learnQuestionsRepository;
        this.topicsRepository = topicsRepository;
        this.learnAnswersRepository = learnAnswersRepository;
        this.subjectsRepository = subjectsRepository;
    }


//    @GetMapping
//    public List findAll() {
//        return repository.findAll();
//    }

    @GetMapping({"/{userId}"})
    public List<LinkedHashMap> findBySubjectId(@PathVariable long userId) {

        List<LinkedHashMap> stats = new ArrayList<>();
        List<Subjects> subjects = subjectsRepository.findAll();

        for (Subjects subject : subjects) {
            LinkedHashMap subjectStats = new LinkedHashMap();
            List<LinkedHashMap> topics = new ArrayList<>();
            int numberOfAnswers = 0;
            int numberOfCorrectAnswers = 0;

            subjectStats.put("subjectId", subject.getId());
            subjectStats.put("subjectName", subject.getName());

            //calcular stats por topico
            List<Topics> topicsModel = topicsRepository.findBySubjectId(subject.getId());
            for (Topics topicModel : topicsModel) {
                LinkedHashMap topicStats = new LinkedHashMap();
                topicStats.put("topicId", topicModel.getId());
                topicStats.put("topicName", topicModel.getName());

                int numberOfTopicAnswers = 0;
                int numberOfCorrectTopicAnswers = 0;

                List<LearnQuestions> topicQuestions = learnQuestionsRepository.findByTopicId(topicModel.getId());

                List<Long> topicQuestionsIds = topicQuestions
                        .stream()
                        .map(it -> it.getId())
                        .collect(Collectors.toList());

                List<LearnAnswers> topicAnswers = learnAnswersRepository.findAll()
                        .stream()
                        .filter(it -> it.getUserId() == userId)
                        .filter(it -> topicQuestionsIds.contains(it.getLearnQuestionsId()))
                        .collect(Collectors.toList());

                final List<LinkedHashMap> joinado = JoinQuestions(topicQuestions, topicAnswers);

                for(LinkedHashMap joinadinho: joinado){
                    if(joinadinho.containsKey("answer"))
                    {
                        numberOfTopicAnswers++;
                        numberOfAnswers++;
                        if((Boolean) joinadinho.get("answer"))
                        {
                            numberOfCorrectTopicAnswers++;
                            numberOfCorrectAnswers++;
                        }
                    }
                }

                topicStats.put("numberOfTopicAnswers", numberOfTopicAnswers);
                topicStats.put("numberOfCorrectTopicAnswers", numberOfCorrectTopicAnswers);
                topics.add(topicStats);
            }

            subjectStats.put("topics", topics);
            subjectStats.put("numberOfAnswers", numberOfAnswers);
            subjectStats.put("numberOfCorrectAnswers", numberOfCorrectAnswers);
            stats.add(subjectStats);
        }

        return stats;
    }
}



