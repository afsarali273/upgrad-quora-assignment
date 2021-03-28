package com.upgrad.quora.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ANSWER", schema = "public")
 @NamedQueries({
        @NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuId"),
         @NamedQuery(name = "getAllAnswersToQuestion",query ="select a from AnswerEntity a where a.question.uuid= :questionUuid" )
 })
public class AnswerEntity implements Serializable {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "UUID")
  @Size(max = 200)
  private String uuid;

  @Column(name = "ANS")
  @NotNull
  @Size(max = 255)
  private String answer;

  @Column(name = "DATE")
  @NotNull
  private Timestamp date;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "QUESTION_ID")
  private QuestionEntity question;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public QuestionEntity getQuestion() {
    return question;
  }

  public void setQuestion(QuestionEntity question) {
    this.question = question;
  }
}
