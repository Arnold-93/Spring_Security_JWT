package com.example.SpringSecurityJWT.infrastructure.abstract_services;

import com.example.SpringSecurityJWT.api.models.request.UserRequest;
import com.example.SpringSecurityJWT.api.models.response.UserResponse;

public interface IUserService extends CrudService<UserRequest, UserResponse, Long>{
}
