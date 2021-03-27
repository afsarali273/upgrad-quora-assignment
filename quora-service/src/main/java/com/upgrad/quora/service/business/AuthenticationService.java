package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider CryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        final String encryptedPassword = CryptographyProvider.encrypt(password, userEntity.getSalt());
        if (encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
            userAuthTokenEntity.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));

            userAuthTokenEntity.setLoginAt(now);
            userAuthTokenEntity.setExpiresAt(expiresAt);

            userDao.createAuthToken(userAuthTokenEntity);

            userDao.updateUser(userEntity);
            return userAuthTokenEntity;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String accessToken) throws SignOutRestrictedException {
        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(accessToken);
        if (userEntity == null)
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");

            final ZonedDateTime now = ZonedDateTime.now();
            userEntity.setLogoutAt(now);
            return userEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(long UserId,final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userEntity = userDao.getUserAuthTokenEntity(accessToken);

        //Check user is signed in
        if (userEntity == null)
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        // Check User is Signed Out
        if(userEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");

       //Check userId is correct/exist in DB
        if(userDao.getUserAuthTokenEntityByUserId(UserId) == null)
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");

        return userEntity;
    }
}


