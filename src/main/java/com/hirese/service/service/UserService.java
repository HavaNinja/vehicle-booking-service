package com.hirese.service.service;

import com.hirese.service.entity.User;
import com.hirese.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUserById(final UUID id) {
        return userRepository.findById(id);
    }
}
