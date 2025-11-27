package com.errday.boardservice.service;

import com.errday.boardservice.client.PointClient;
import com.errday.boardservice.client.UserClient;
import com.errday.boardservice.domain.Board;
import com.errday.boardservice.domain.BoardRepository;
import com.errday.boardservice.dto.BoardResponseDto;
import com.errday.boardservice.dto.CreateBoardRequestDto;
import com.errday.boardservice.dto.UserDto;
import com.errday.boardservice.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final int BOARD_SAVE_POINTS = 100;
    private final int BOARD_ACTIVITY_POINTS = 10;
    private final BoardRepository boardRepository;
    private final UserClient userClient;
    private final PointClient pointClient;

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

            userClient.addActivityScore(createBoardRequestDto.userId(), BOARD_ACTIVITY_POINTS);
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

        Optional<UserResponseDto> optionalUserResponseDto = userClient.fetchUser(boardId);

        UserDto userDto = null;
        if (optionalUserResponseDto.isPresent()) {
            UserResponseDto userResponseDto = optionalUserResponseDto.get();
            userDto = new UserDto(userResponseDto.userId(), userResponseDto.name());
        }

        return new BoardResponseDto(board.getTitle(), board.getContent(), userDto);
    }

    public List<BoardResponseDto> getAllBoards() {
        List<Board> boards = boardRepository.findAll();

        List<Long> userIds = boards.stream()
                .map(Board::getUserId)
                .distinct()
                .toList();

        List<UserResponseDto> userResponseDtos = userClient.fetchAllUsersByIds(userIds);

        Map<Long, UserDto> userMap = new HashMap<>();
        for (UserResponseDto userResponseDto : userResponseDtos) {
            Long userId = userResponseDto.userId();
            String name = userResponseDto.name();
            userMap.put(userId, new UserDto(userResponseDto.userId(), name));
        }

        return boards.stream()
                .map(board -> new BoardResponseDto(board.getTitle(), board.getContent(), userMap.get(board.getUserId())))
                .toList();
    }
}
