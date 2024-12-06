package com.example.musicHub;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateUserMbti(UserEntity user) {
        userRepository.save(user);
    }
}
