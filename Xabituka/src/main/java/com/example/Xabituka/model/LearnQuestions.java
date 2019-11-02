package com.example.Xabituka.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
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
    
    public static LearnQuestions applyHeuristic(List <LearnQuestions> questions,  List <LearnAnswers> answers){
        double dmax = 5;
        double davg = 3;
        double navg = 5.5;
        double e = (davg - 1)*(navg - 1);
        double totalWeight  = 0;
        
        final List<LinkedHashMap> joinado = JoinQuestions(questions, answers);
        
        for(int idx = 0; idx < joinado.size(); idx++){
            double nrtc = 0;
            double nrt = 0;
            double srt = 0;
            double alpha;
            double drt;
            double nt;
            double ep;
            
            for(LinkedHashMap joinadinho: joinado){
                if(joinadinho.get("topicId").equals(joinado.get(idx).get("topicId")))
                {
                    if((Boolean) joinadinho.get("answer"))
                    {
                        nrtc++;
                    }
                    if(joinadinho.get("answer") != null)
                    {
                        nrt++;
                        srt += (Integer) joinadinho.get("difficulty");
                    }
                
                }
            }
                
            alpha = nrtc/nrt;
            drt = srt/nrt;
            nt = drt + dmax*alpha;
            ep = (navg - nt)*(davg - (Integer) joinado.get(idx).get("difficulty")) + e;
            totalWeight += ep;
            
            joinado.get(idx).put("weight", ep);
        }
        
        // rodar roleta
        
        
        
        
        
    }
    
    public static List<LinkedHashMap> JoinQuestions(List <LearnQuestions> questions,  List <LearnAnswers> answers){
        List<LinkedHashMap> ans = new ArrayList<>();
        
        for(LearnQuestions question : questions){
            LinkedHashMap questionMap = new LinkedHashMap();
            questionMap.put("questionId",question.getId());
            questionMap.put("topicId",question.getTopicId());
            questionMap.put("difficulty",question.getDifficulty());
            
            for(LearnAnswers answer : answers){
                if (answer.getLearnQuestionsId().equals(question.getId())){
                    questionMap.put("answer",answer.isGotItRight());
                    break;
                }
            }
            
            ans.add(questionMap);
        }
        
        return ans;
    }
    
}
