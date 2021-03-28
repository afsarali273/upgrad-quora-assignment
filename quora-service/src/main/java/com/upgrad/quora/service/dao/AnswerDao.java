package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

  @PersistenceContext private EntityManager entityManager;

  public AnswerEntity createAnswer(AnswerEntity answerEntity) {
    entityManager.persist(answerEntity);
    return answerEntity;
  }

  public AnswerEntity getAnswerById(final String answerId) {
    try {
      return entityManager
              .createNamedQuery("answerByUuid", AnswerEntity.class)
              .setParameter("uuId", answerId)
              .getSingleResult();
    } catch (Exception nre) {
      return null;
    }
  }

  public void updateAnswer(AnswerEntity answerEntity) {
    entityManager.merge(answerEntity);
  }

  public AnswerEntity deleteAnswer(final String answerId) {
    AnswerEntity deleteAnswer = getAnswerById(answerId);
    if (deleteAnswer != null) {
      entityManager.remove(deleteAnswer);
    }
    return deleteAnswer;
  }


  public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
    return entityManager
            .createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class)
            .setParameter("questionUuid", questionId)
            .getResultList();
  }
}
