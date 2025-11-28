package com.errday.boardservice.service;

import com.errday.boardservice.domain.User;
import com.errday.boardservice.domain.UserRepository;
import com.errday.boardservice.dto.SaveUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(SaveUserRequestDto saveUserRequestDto) {
        var user = new User(saveUserRequestDto.userId(), saveUserRequestDto.name());
        userRepository.save(user);
    }
}
