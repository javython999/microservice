package com.errday.userservice.controller;

import com.errday.userservice.dto.AddActivityScoreRequestDto;
import com.errday.userservice.dto.UserResponseDto;
import com.errday.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UserInternalController {

    private final UserService userService;


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsersByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.getUsersByIds(ids));
    }

    @PostMapping("/activity-score/add")
    public ResponseEntity<Void> addActivityScore(@RequestBody AddActivityScoreRequestDto addActivityScoreRequestDto) {
        userService.addActivityScore(addActivityScoreRequestDto);
        return ResponseEntity.noContent().build();
    }

}
