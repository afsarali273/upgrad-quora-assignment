package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(authorization);

        //Check user is signed in
        if (userEntity == null)
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        // Check User is Signed Out
        if(userEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        return userEntity;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        questionDao.createQuestion(questionEntity);
        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(String authToken, String questionId , QuestionEntity questionEntity) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(authToken);
        //Check user is signed in
        if (userEntity == null)
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        // Check User is Signed Out
        if(userEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");

        // Check only owner can edit the question
       String questionOwner = questionDao.findQuestionById(questionId).getUser().getUserName();
       String loggedInUser =  userEntity.getUser().getUserName();

       if(!loggedInUser.equals(questionOwner))
           throw new AuthorizationFailedException("ATHR-003","Only the question owner can edit the question");

       // Check Question is Present in DB
        if(questionDao.findQuestionById(questionId) ==null)
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");

       return questionDao.updateQuestion(questionEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String authToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(authToken);
        //Check user is signed in
        if (userEntity == null)
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        // Check User is Signed Out
        if(userEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete a question");

        // Check only owner/admin can edit the question
        String questionOwner = questionDao.findQuestionById(questionId).getUser().getUserName();
        String loggedInUser =  userEntity.getUser().getUserName();
        if(!loggedInUser.equals(questionOwner) || !userEntity.getUser().getRole().contains("admin"))
            throw new AuthorizationFailedException("ATHR-003","Only the question owner or admin can delete the question");

        // Check Question is Present in DB
        if(questionDao.findQuestionById(questionId) ==null)
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");

        return questionDao.deleteQuestion(questionId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionByUser(String authToken, String userId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(authToken);
        //Check user is signed in
        if (userEntity == null)
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        // Check User is Signed Out
        if(userEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");

        // Check Question is Present in DB
        if(userDao.getUserByUuid(userEntity.getUser().getUuid()) ==null)
            throw new InvalidQuestionException("QUES-001","User with entered uuid whose question details are to be seen does not exist");

        return questionDao.getAllQuestionsByUserId(userId);
    }

    public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {
        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(authorization);
        //Check user is signed in
        if (userEntity == null)
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        // Check User is Signed Out
        if(userEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
       return questionDao.getAllQuestions();

    }
}