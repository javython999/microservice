package com.errday.userservice.consumer;

import com.errday.userservice.dto.AddActivityScoreRequestDto;
import com.errday.userservice.event.BoardCreatedEvent;
import com.errday.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardCreatedEventConsumer {

    private final int BOARD_ACTIVITY_SCORE = 10;
    private final UserService userService;

    @KafkaListener(
            topics = "board.created",
            groupId = "user-service"
    )
    public void consume(String message) {
        var boardCreatedEvent = BoardCreatedEvent.fromJson(message);

        var addActivityScoreRequestDto = new AddActivityScoreRequestDto(boardCreatedEvent.userId(), BOARD_ACTIVITY_SCORE);

        userService.addActivityScore(addActivityScoreRequestDto);
    }
}
