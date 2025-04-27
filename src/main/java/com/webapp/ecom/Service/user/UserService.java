package com.webapp.ecom.Service.user;

import com.webapp.ecom.dto.UserDto;
import com.webapp.ecom.exceptions.AlreadyExistsException;
import com.webapp.ecom.exceptions.ResourceNotFoundException;
import com.webapp.ecom.model.User;
import com.webapp.ecom.repository.UserRepository;
import com.webapp.ecom.request.CreateUserRequest;
import com.webapp.ecom.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()->new AlreadyExistsException(request.getEmail()+" already exist"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map((existingUser)->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
            }).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,()-> {
            throw new ResourceNotFoundException("User not found");
        });
    }

    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }
}
