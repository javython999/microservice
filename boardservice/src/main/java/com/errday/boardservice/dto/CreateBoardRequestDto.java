package com.errday.boardservice.dto;

public record CreateBoardRequestDto(String title, String content, Long userId) {
}
