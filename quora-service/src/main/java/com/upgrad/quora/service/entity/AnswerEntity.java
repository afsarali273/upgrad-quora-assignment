package com.upgrad.quora.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// TODO -- Need to change entity referring Schema
@Entity
@Table(name = "QUESTION", schema = "public")
// @NamedQueries({
//        @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email =
// :email")
// })
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
}
