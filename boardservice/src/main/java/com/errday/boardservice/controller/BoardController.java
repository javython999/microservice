package com.errday.boardservice.controller;

import com.errday.boardservice.dto.BoardResponseDto;
import com.errday.boardservice.dto.CreateBoardRequestDto;
import com.errday.boardservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody CreateBoardRequestDto createBoardRequestDto) {
        boardService.create(createBoardRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @GetMapping()
    public ResponseEntity<List<BoardResponseDto>> getBoard() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
}
