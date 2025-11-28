package com.errday.userservice.event;

import tools.jackson.databind.ObjectMapper;

public record BoardCreatedEvent(long userId) {

    public static BoardCreatedEvent fromJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, BoardCreatedEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패");
        }
    }
}
