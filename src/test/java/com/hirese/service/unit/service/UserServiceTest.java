package com.hirese.service.unit.service;

import com.hirese.service.entity.User;
import com.hirese.service.repository.UserRepository;
import com.hirese.service.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;

public class UserServiceTest extends BaseServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @After
    public void afterEach() {
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldReturnUserById() {
        final UUID userId = UUID.randomUUID();
        final User user = new User();
        user.setUserId(userId);

        Mockito.when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        final Optional<User> userById = userService.getUserById(userId);

        Assert.assertEquals(user, userById.get());

        Mockito.verify(userRepository).findById(eq(userId));
    }
}