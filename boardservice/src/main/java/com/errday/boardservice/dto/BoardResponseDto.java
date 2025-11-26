package com.errday.boardservice.dto;

public record BoardResponseDto(String title, String content, UserDto user) {
}
