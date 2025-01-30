package org.ubb.image_handler_service.service.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ubb.image_handler_service.dto.auth.LoginRequest;
import org.ubb.image_handler_service.exception.UsernameTakenException;
import org.ubb.image_handler_service.model.UserEntity;
import org.ubb.image_handler_service.repository.UserRepository;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String password)
    {
        if (userRepository.findByUsername(username).isPresent())
        {
            throw new UsernameTakenException("Username already taken!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public UserEntity getUserByUsername(String username)
    {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void authenticateUser(LoginRequest loginRequest)
    {
        UserEntity userEntity = getUserByUsername(loginRequest.username());
        // Password is not encoded correctly
        if (!passwordEncoder.matches(loginRequest.password(), userEntity.getPassword()))
        {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
