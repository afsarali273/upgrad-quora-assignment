package com.upgrad.quora.service.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {


    @PersistenceContext
    private EntityManager entityManager;

//    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
//        entityManager.persist(questionEntity);
//        return questionEntity;
//    }

//    public void updateUser(final UserEntity updatedUserEntity) {
//        entityManager.merge(updatedUserEntity);
//    }
//
//    public void deleteUser(final UserEntity deleteUser) {
//        entityManager.createNamedQuery("deleteUserAuthTokenByUserId", UserAuthTokenEntity.class).setParameter("userId",deleteUser.getId()).getSingleResult();
//        entityManager.createNamedQuery("deleteByUserName", UserEntity.class).setParameter("username",deleteUser.getUserName() ).getSingleResult();
//    }


}
