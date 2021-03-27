package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {


    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions() {
      return  entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
    }

    public List<QuestionEntity> getAllQuestionsByUserId(String userId) {
        return  entityManager.createNamedQuery("allQuestionsByUserId", QuestionEntity.class).setParameter("userId",userId).getResultList();
    }

    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
        return  entityManager.merge(questionEntity);
    }

    // Finding question based on its Id
    public QuestionEntity findQuestionById(String questionId) {
        return  entityManager.createNamedQuery("findByQuestionId", QuestionEntity.class).setParameter("questionId",questionId).getSingleResult();
    }

    public QuestionEntity deleteQuestion(String questionId) {
        QuestionEntity questionEntity =  findQuestionById(questionId);
        entityManager.createNamedQuery("deleteByQuestionId",QuestionEntity.class).setParameter("questionId",questionId);
        return questionEntity;
    }


}
