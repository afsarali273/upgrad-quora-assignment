package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthToken = questionService.authenticate(authorization);
        UserEntity user = userAuthToken.getUser();

        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(Timestamp.valueOf(LocalDateTime.now()));
        questionEntity.setUser(user);
        final QuestionEntity createdQuestion = questionService.createQuestion(questionEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        final List<QuestionEntity> allQuestions = questionService.getAllQuestions(authorization);

        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        allQuestions.stream().forEach(question -> {
                    QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse()
                            .content(question.getContent())
                            .id(question.getUuid());
                    questionDetailsResponseList.add(questionDetailsResponse);
                }
        );
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@PathVariable("questionId") String questionId, final QuestionEditRequest questionEditRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        final UserAuthTokenEntity userAuthTokenEntity = questionService.authenticate(authorization, questionId);

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionEditRequest.getContent());
        questionEntity.setDate(Timestamp.valueOf(LocalDateTime.now()));
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setUser(userAuthTokenEntity.getUser());

        questionService.updateQuestion(questionEntity);

        QuestionEditResponse questionEditResponse = new QuestionEditResponse();
        questionEditResponse.id(questionEntity.getUuid());
        questionEditResponse.status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = questionService.deleteQuestion(authorization, questionId);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getQuestionsByUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {

        List<QuestionEntity> questionEntities = questionService.getAllQuestionByUser(authorization, userId);
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();

        questionEntities.stream().forEach(question -> {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse()
                    .id(question.getUuid())
                    .content(question.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        });

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }
}
