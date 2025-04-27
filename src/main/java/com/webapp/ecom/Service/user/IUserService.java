package com.webapp.ecom.Service.user;

import com.webapp.ecom.dto.UserDto;
import com.webapp.ecom.model.User;
import com.webapp.ecom.request.CreateUserRequest;
import com.webapp.ecom.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
