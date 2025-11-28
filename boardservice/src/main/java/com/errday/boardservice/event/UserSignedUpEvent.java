package com.errday.boardservice.event;

import tools.jackson.databind.ObjectMapper;

public record UserSignedUpEvent(long userId, String name) {

    public static UserSignedUpEvent fromJson(String jsonString) {
        return new ObjectMapper().readValue(jsonString, UserSignedUpEvent.class);
    }
}
