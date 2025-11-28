package com.errday.boardservice.consumer;

import com.errday.boardservice.dto.SaveUserRequestDto;
import com.errday.boardservice.event.UserSignedUpEvent;
import com.errday.boardservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSignedUpEventConsumer {

    private final UserService userService;

    @KafkaListener(
            topics = "user.signed-up",
            groupId = "board-service"
    )
    public void consume(String message) {
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.fromJson(message);

        var saveUserRequest = new SaveUserRequestDto(userSignedUpEvent.userId(), userSignedUpEvent.name());

        userService.save(saveUserRequest);
    }
}
