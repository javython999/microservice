package com.errday.userservice.service;

import com.errday.userservice.client.PointClient;
import com.errday.userservice.domain.User;
import com.errday.userservice.dto.AddActivityScoreRequestDto;
import com.errday.userservice.dto.SignUpRequestDto;
import com.errday.userservice.domain.UserRepository;
import com.errday.userservice.dto.UserResponseDto;
import com.errday.userservice.event.UserSignedUpEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointClient pointClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        var user = new User(
                signUpRequestDto.email(),
                signUpRequestDto.name(),
                signUpRequestDto.password()
        );
        User savedUser = userRepository.save(user);

        int WELCOME_POINT = 1000;
        pointClient.addPoints(savedUser.getUserId(), WELCOME_POINT);

        var userSignedUpEvent = new UserSignedUpEvent(savedUser.getUserId(), savedUser.getName());

        kafkaTemplate.send("user.signed-up", toJsonString(userSignedUpEvent));
    }

    public UserResponseDto getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
        return new UserResponseDto(user.getUserId(), user.getEmail(), user.getName());
    }

    public List<UserResponseDto> getUsersByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);

        return users.stream()
                .map(user -> new UserResponseDto(user.getUserId(), user.getEmail(), user.getName()))
                .toList();
    }

    @Transactional
    public void addActivityScore(AddActivityScoreRequestDto addActivityScoreRequestDto) {
        User user = userRepository.findById(addActivityScoreRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + addActivityScoreRequestDto.userId() + " not found"));

        user.addActivityScore(addActivityScoreRequestDto.score());

        userRepository.save(user);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }
    }

    private String toJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Json 직렬화 실패");
        }
    }

}
