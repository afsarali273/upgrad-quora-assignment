package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> login(@PathVariable("userId") String userId,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthToken = authenticationService.authenticate(Long.parseLong(userId),authorization);
        UserEntity user = userAuthToken.getUser();

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse.userName(user.getUserName());
        userDetailsResponse.firstName(user.getFirstName());
        userDetailsResponse.lastName(user.getLastName());
        userDetailsResponse.dob(user.getDob());
        userDetailsResponse.aboutMe(user.getAboutMe());
        userDetailsResponse.country(user.getCountry());
        userDetailsResponse.contactNumber(user.getContactNumber());
        userDetailsResponse.emailAddress(user.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthToken.getAccessToken());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, headers, HttpStatus.OK);
    }
}
