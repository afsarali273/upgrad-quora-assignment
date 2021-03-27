package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    @Autowired
    UserDao userDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(long UserId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(authorization);

        //Check user is signed in
        if (userEntity == null)
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        // Check User is Signed Out
        if(userEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out'");

        // Check User is Admin
        if(userEntity.getUser().getRole().contains("nonadmin"))
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");

        //Check userId is correct/exist in DB
        if(userDao.getUserAuthTokenEntityByUserId(UserId) == null)
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");

        return userEntity;
    }

    //Delete User
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(UserEntity userEntity){
        userDao.deleteUser(userEntity);
    }

}
