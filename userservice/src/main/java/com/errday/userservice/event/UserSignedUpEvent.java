package com.errday.userservice.event;

public record UserSignedUpEvent(long userId, String name) {
}
