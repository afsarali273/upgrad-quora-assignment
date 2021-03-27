package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {


    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByUserName(final String username) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByUuid(final String Uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", Uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    // Retrieve User by using AccessToken
    public UserAuthTokenEntity getUserAuthTokenEntity(String accessToken) {
        try {
           return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    // Retrieve User by using UserId
    public UserAuthTokenEntity getUserAuthTokenEntityByUserId(long userId) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByUserId", UserAuthTokenEntity.class).setParameter("userId", userId).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    public void deleteUser(final UserEntity deleteUser) {
        entityManager.createNamedQuery("deleteUserAuthTokenByUserId", UserAuthTokenEntity.class).setParameter("userId",deleteUser.getId()).getSingleResult();
        entityManager.createNamedQuery("deleteByUserName", UserEntity.class).setParameter("username",deleteUser.getUserName() ).getSingleResult();
    }


}
