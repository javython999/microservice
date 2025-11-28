package com.errday.boardservice.service;

import com.errday.boardservice.client.PointClient;
import com.errday.boardservice.client.UserClient;
import com.errday.boardservice.domain.Board;
import com.errday.boardservice.domain.BoardRepository;
import com.errday.boardservice.domain.User;
import com.errday.boardservice.dto.BoardResponseDto;
import com.errday.boardservice.dto.CreateBoardRequestDto;
import com.errday.boardservice.dto.UserDto;
import com.errday.boardservice.dto.UserResponseDto;
import com.errday.boardservice.event.BoardCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final int BOARD_SAVE_POINTS = 100;
    private final BoardRepository boardRepository;
    private final UserClient userClient;
    private final PointClient pointClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    //@Transactional
    public void create(CreateBoardRequestDto createBoardRequestDto) {
        boolean isBoardCreated = false;
        Long savedBoardId = null;

        boolean isPoinDteducted = false;

        try {
            pointClient.deductPoints(createBoardRequestDto.userId(), BOARD_SAVE_POINTS);
            isPoinDteducted = true;

            var board = new Board(createBoardRequestDto.title(), createBoardRequestDto.content(), createBoardRequestDto.userId());
            Board savedBoard = boardRepository.save(board);
            savedBoardId = savedBoard.getBoardId();
            isBoardCreated = true;

            var boardCreatedEvent = new BoardCreatedEvent(createBoardRequestDto.userId());
            kafkaTemplate.send("board.created",toJsonString(boardCreatedEvent));
        } catch (Exception e) {
            if (isBoardCreated) {
                boardRepository.deleteById(savedBoardId);
            }

            if (isPoinDteducted) {
                pointClient.addPoints(createBoardRequestDto.userId(), BOARD_SAVE_POINTS);
            }

            throw e;
        }


    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        return new BoardResponseDto(
                board.getTitle(),
                board.getContent(),
                new UserDto(board.getUserId(), board.getUserName())
        );
    }

    public List<BoardResponseDto> getAllBoards() {
        List<Board> boards = boardRepository.findAll();

        return boards.stream()
                .map(board -> new BoardResponseDto(
                        board.getTitle(),
                        board.getContent(),
                        new UserDto(board.getUserId(), board.getUserName()))
                )
                .toList();
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
