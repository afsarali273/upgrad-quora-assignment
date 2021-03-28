package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao {

  @PersistenceContext private EntityManager entityManager;

  public QuestionEntity createQuestion(QuestionEntity questionEntity) {
    entityManager.persist(questionEntity);
    return questionEntity;
  }

  public List<QuestionEntity> getAllQuestions() {
    return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
  }

  public List<QuestionEntity> getAllQuestionsByUserId(String userId) {
    return entityManager
        .createNamedQuery("allQuestionsByUserId", QuestionEntity.class)
        .setParameter("userId", userId)
        .getResultList();
  }

  public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
    return entityManager.merge(questionEntity);
  }

  // Finding question based on its Id
  public QuestionEntity findQuestionByUuid(String questionUuid) {
    try {
      return entityManager
          .createNamedQuery("findByQuestionUuId", QuestionEntity.class)
          .setParameter("questionUuid", questionUuid)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public QuestionEntity deleteQuestion(String questionUuid) {
    QuestionEntity questionEntity = findQuestionByUuid(questionUuid);
    entityManager.remove(questionEntity);
    return questionEntity;
  }
}
