package com.errday.pointservice.controller;

import com.errday.pointservice.dto.AddPointRequestDto;
import com.errday.pointservice.dto.DeductPointRequestDto;
import com.errday.pointservice.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;

    @PostMapping("/add")
    public ResponseEntity<Void> addPoint(@RequestBody AddPointRequestDto addPointRequestDto) {
        pointService.addPoints(addPointRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deduct")
    public ResponseEntity<Void> deduct(@RequestBody DeductPointRequestDto deductPointRequestDto) {
        pointService.deductPoints(deductPointRequestDto);
        return ResponseEntity.noContent().build();
    }
}
